package com.authserver.authserver.code_note;

import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.code_note.entry.AddCodeNoteRequest;
import com.authserver.authserver.code_note.entry.CodeNoteEntry;
import com.authserver.authserver.code_note.managers.CodeNoteManager;

@Service
public class CodeNoteService extends BaseService<Long, CodeNoteEntry, CodeNoteManager> {

    public CodeNoteService(CodeNoteManager manager) {
        super(manager);
    }

    public ResponseEntity<BaseResponse<CodeNoteEntry>> addByPosition(AddCodeNoteRequest request) {
        return handleServiceOperation(() -> Collections.singletonList(manager.addByPosition(request)), "Added successfully", null, null);
    }
}
