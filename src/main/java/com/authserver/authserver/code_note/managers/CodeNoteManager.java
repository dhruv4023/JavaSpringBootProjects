package com.authserver.authserver.code_note.managers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.code_note.Models.CodeNote;
import com.authserver.authserver.code_note.convertor.CodeNoteConvertor;
import com.authserver.authserver.code_note.entry.AddCodeNoteRequest;
import com.authserver.authserver.code_note.entry.AddPrCodeNoteRequest;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.inngest.NoteCreatedEventQueue;
import com.authserver.authserver.code_note.repository.CodeNoteRepository;
import com.authserver.authserver.code_note.utils.GithubLinkParser;
import com.authserver.authserver.event_queue.EventQueueEntry;
import com.authserver.authserver.user.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;

@Component
public class CodeNoteManager extends BaseManager<Long, CodeNoteEntry, CodeNote, CodeNoteRepository> {

    private final ObjectMapper objectMapper;

    private final NoteCreatedEventQueue noteCreatedEventQueue;

    private final CodeNoteConvertor codeNoteConvertor;
    private final SecurityUtils securityUtils;
    private final GithubLinkParser githubLinkParser;

    protected CodeNoteManager(CodeNoteRepository repository, CodeNoteConvertor codeNoteConvertor,
            SecurityUtils securityUtils, NoteCreatedEventQueue noteCreatedEventQueue, ObjectMapper objectMapper,
            GithubLinkParser githubLinkParser) {
        super(repository, "code note");
        this.codeNoteConvertor = codeNoteConvertor;
        this.securityUtils = securityUtils;
        this.noteCreatedEventQueue = noteCreatedEventQueue;
        this.objectMapper = objectMapper;
        this.githubLinkParser = githubLinkParser;
    }

    @Override
    protected CodeNote toEntity(CodeNoteEntry entry, CodeNote existing) throws EntityNotFoundException {
        return codeNoteConvertor.toModel(entry, existing);
    }

    @Override
    protected CodeNoteEntry toEntry(CodeNote entity) throws EntityNotFoundException {
        return codeNoteConvertor.toEntry(entity);
    }

    @Override
    @Transactional
    public CodeNoteEntry add(CodeNoteEntry entry) throws EntityNotFoundException {
        if (Objects.isNull(entry.getPosition())) {
            calculatePositions(null, null, Collections.singletonList(entry));
        }
        CodeNoteEntry note = super.add(entry);
        try {
            noteCreatedEventQueue.addToQueue(
                    new EventQueueEntry(securityUtils.getCurrentUserId(), objectMapper.writeValueAsString(note), 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return note;
    }

    public CodeNoteEntry addByPosition(AddCodeNoteRequest request) {
        CodeNoteEntry entry = request.getEntry();
        calculatePositions(request.getBeforeId(), request.getAfterId(), Collections.singletonList(entry));
        return add(entry);
    }

    @Override
    protected Sort getSort() {
        Sort sort = super.getSort();
        Objects.requireNonNull(sort, "Sort must not be null");
        return Sort.by("position").and(sort);
    }

    public Page<CodeNoteEntry> getManyCommitsByNotebookId(long notebookId, long page, long size) {
        Pageable pageable = PageRequest.of((int) page, (int) size, getSort());
        Page<CodeNote> entityPage = repository.findByNotebookId(notebookId, pageable);
        return entityPage.map(this::toEntry);
    }

    public List<CodeNoteEntry> addPrNotesByPosition(AddPrCodeNoteRequest request) {
        List<CodeNoteEntry> entries = githubLinkParser.processPR(request.getPrLink(), request.getNotebookId());
        calculatePositions(request.getBeforeId(), request.getAfterId(), entries);
        List<CodeNoteEntry> result = repository.saveAll(entries.stream().map(entry -> toEntity(entry, null)).toList()).stream()
                .map(this::toEntry).toList();
        result.forEach(note -> {
            try {
                noteCreatedEventQueue.addToQueue(
                        new EventQueueEntry(securityUtils.getCurrentUserId(), objectMapper.writeValueAsString(note),
                                0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    private void calculatePositions(Long beforeId, Long afterId, List<CodeNoteEntry> entries) {

        if (entries == null || entries.isEmpty())
            return;

        Long notebookId = entries.get(0).getNotebookId();

        // Case 1: both null → add at bottom
        if (beforeId == null && afterId == null) {

            Long maxPosition = repository.findMaxPositionByNotebookId(notebookId);
            long start = (maxPosition == null ? 0L : maxPosition);

            for (int i = 0; i < entries.size(); i++) {
                entries.get(i).setPosition(start + (i + 1) * 1000);
            }
            return;
        }

        // Fetch before/after
        Set<Long> ids = new HashSet<>();
        if (beforeId != null)
            ids.add(beforeId);
        if (afterId != null)
            ids.add(afterId);

        Map<Long, CodeNote> noteMap = repository
                .findByIdInAndNotebookId(ids, notebookId)
                .stream()
                .collect(Collectors.toMap(CodeNote::getId, n -> n));

        CodeNote before = beforeId != null ? noteMap.get(beforeId) : null;
        CodeNote after = afterId != null ? noteMap.get(afterId) : null;

        if (beforeId != null && before == null)
            throw new EntityNotFoundException("Before note not found");

        if (afterId != null && after == null)
            throw new EntityNotFoundException("After note not found");

        // Case 2: Insert between
        if (before != null && after != null) {

            long beforePos = before.getPosition();
            long afterPos = after.getPosition();

            long totalSpace = afterPos - beforePos;

            // Not enough space → rebalance ONCE
            if (totalSpace <= entries.size()) {
                repository.rebalancePositions(notebookId, Math.max(entries.size() + 1000L, 1000L));
                // fetch fresh positions
                calculatePositions(beforeId, afterId, entries);
                return;
            }

            long gap = totalSpace / (entries.size() + 1);

            for (int i = 0; i < entries.size(); i++) {
                entries.get(i).setPosition(beforePos + (i + 1) * gap);
            }
            return;
        }

        // Case 3: Insert after a note (bottom-like)
        if (before != null) {

            long start = before.getPosition();

            for (int i = 0; i < entries.size(); i++) {
                entries.get(i).setPosition(start + (i + 1) * 1000);
            }
            return;
        }

        // Case 4: Insert before a note (top-like)
        Objects.requireNonNull(after);

        long afterPos = after.getPosition();
        long gap = afterPos / (entries.size() + 1);

        if (gap <= 0) {
            repository.rebalancePositions(notebookId, Math.max(entries.size() + 1000L, 1000L));
            calculatePositions(beforeId, afterId, entries);
            return;
        }

        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).setPosition((i + 1) * gap);
        }
    }
}
