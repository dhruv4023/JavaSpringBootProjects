package com.authserver.authserver.code_note.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.authserver.authserver.base.BaseManager;
import com.authserver.authserver.code_note.Models.CodeNoteModel;
import com.authserver.authserver.code_note.convertor.CodeNoteConvertor;
import com.authserver.authserver.code_note.entry.AddCodeNoteRequest;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.inngest.NoteCreatedEventQueue;
import com.authserver.authserver.code_note.repository.CodeNoteRepository;
import com.authserver.authserver.event_queue.EventQueueEntry;
import com.authserver.authserver.user.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;

@Component
public class CodeNoteManager extends BaseManager<Long, CodeNoteEntry, CodeNoteModel, CodeNoteRepository> {

    private final ObjectMapper objectMapper;

    private final NoteCreatedEventQueue noteCreatedEventQueue;

    private final CodeNoteConvertor codeNoteConvertor;
    private final SecurityUtils securityUtils;

    protected CodeNoteManager(CodeNoteRepository repository, CodeNoteConvertor codeNoteConvertor,
            SecurityUtils securityUtils, NoteCreatedEventQueue noteCreatedEventQueue, ObjectMapper objectMapper) {
        super(repository, "code note");
        this.codeNoteConvertor = codeNoteConvertor;
        this.securityUtils = securityUtils;
        this.noteCreatedEventQueue = noteCreatedEventQueue;
        this.objectMapper = objectMapper;
    }

    @Override
    protected CodeNoteModel toEntity(CodeNoteEntry entry, CodeNoteModel existing) throws EntityNotFoundException {
        return codeNoteConvertor.toModel(entry, existing);
    }

    @Override
    protected CodeNoteEntry toEntry(CodeNoteModel entity) throws EntityNotFoundException {
        return codeNoteConvertor.toEntry(entity);
    }

    @Override
    @Transactional
    public CodeNoteEntry add(CodeNoteEntry entry) throws EntityNotFoundException {
        if (Objects.isNull(entry.getPosition())) {
            entry.setPosition(calculatePosition(null, null,
                    securityUtils.getCurrentUserId()));
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
        Long position = calculatePosition(request.getBeforeId(), request.getAfterId(),
                securityUtils.getCurrentUserId());
        entry.setPosition(position);
        return add(entry);
    }

    private Long calculatePosition(Long afterId, Long beforeId, Long userId) {
        // If both null → add at bottom
        if (beforeId == null && afterId == null) {
            Long maxPosition = repository.findMaxPositionByUserId(userId);
            return maxPosition == null ? 1000L : maxPosition + 1000;
        }

        Set<Long> ids = new HashSet<>();

        if (beforeId != null)
            ids.add(beforeId);
        if (afterId != null)
            ids.add(afterId);

        List<CodeNoteModel> notes = repository.findByIdInAndUser_Id(ids, userId);

        Map<Long, CodeNoteModel> noteMap = notes.stream()
                .collect(Collectors.toMap(CodeNoteModel::getId, n -> n));

        CodeNoteModel before = beforeId != null ? noteMap.get(beforeId) : null;

        CodeNoteModel after = afterId != null ? noteMap.get(afterId) : null;

        if (beforeId != null && before == null)
            throw new EntityNotFoundException("Before note not found");

        if (afterId != null && after == null)
            throw new EntityNotFoundException("After note not found");

        // Insert between
        if (before != null && after != null) {
            long gap = Math.abs(after.getPosition() - before.getPosition());

            if (gap <= 1) {
                repository.rebalancePositions(userId);
                return calculatePosition(beforeId, afterId, userId);
            }
            return (before.getPosition() + after.getPosition()) / 2;
        }

        // Insert at bottom
        if (before != null) {
            return before.getPosition() + 1000;
        }

        Objects.requireNonNull(after);
        // Insert at top
        return after.getPosition() - 1000;
    }

    @Override
    protected Sort getSort() {
        Sort sort = super.getSort();
        Objects.requireNonNull(sort, "Sort must not be null");
        return Sort.by("position").descending().and(sort);
    }
}
