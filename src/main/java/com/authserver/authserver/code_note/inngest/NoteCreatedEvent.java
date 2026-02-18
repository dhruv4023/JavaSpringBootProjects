package com.authserver.authserver.code_note.inngest;

import com.authserver.authserver.Inngest.AbstractInngestEvent;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class NoteCreatedEvent extends AbstractInngestEvent<CodeNoteEntry> {

    public void trigger(List<CodeNoteEntry> notes) {
        sendEvent("note/created", notes);
    }
}
