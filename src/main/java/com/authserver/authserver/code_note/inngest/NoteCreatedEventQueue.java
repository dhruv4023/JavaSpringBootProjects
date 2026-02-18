package com.authserver.authserver.code_note.inngest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.event_queue.EventQueueEntry;
import com.authserver.authserver.event_queue.QueueHandler;
import com.authserver.authserver.event_queue.repository.EventQueueRepository;
import com.authserver.authserver.event_queue.repository.FailedEventRepository;
import com.authserver.authserver.user.manager.UserManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class NoteCreatedEventQueue extends QueueHandler {

    private final NoteCreatedEvent noteCreatedEvent;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NoteCreatedEventQueue(EventQueueRepository queueRepo, UserManager userManager,
            FailedEventRepository failedRepo,
            NoteCreatedEvent noteCreatedEvent) {
        super(queueRepo, userManager, failedRepo);
        this.noteCreatedEvent = noteCreatedEvent;
    }

    @Override
    public String eventType() {
        return "note/created";
    }

    @Override
    protected void send(EventQueueEntry events) throws Exception {
        throw new UnsupportedOperationException("Not nneded for this event");
    }

    @Override
    protected List<EventQueueEntry> send(List<EventQueueEntry> events) throws Exception {

        List<CodeNoteEntry> notes = new ArrayList<>();

        for (EventQueueEntry event : events) {

            CodeNoteEntry note = objectMapper.readValue(
                    event.getPayload(),
                    CodeNoteEntry.class);

            notes.add(note);
        }

        noteCreatedEvent.trigger(notes);

        return List.of();
    }

    @Override
    public boolean limitOnePerSender() {
        return false;
    }
}
