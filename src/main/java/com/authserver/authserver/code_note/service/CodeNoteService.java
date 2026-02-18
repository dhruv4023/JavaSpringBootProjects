package com.authserver.authserver.code_note.service;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.authserver.authserver.base.helper.PageResponse;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.code_note.entry.AddCodeNoteRequest;
import com.authserver.authserver.code_note.entry.AddPrCodeNoteRequest;
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

    public ResponseEntity<BaseResponse<CodeNoteEntry>> getManyCommitFIleByNotebookId(long notebookId, long page,
            long size) {
        Page<CodeNoteEntry> result = manager.getManyCommitsByNotebookId(notebookId, page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return ResponseEntity.ok(new BaseResponse<>(true, "Fetched successfully", result.getContent(), pageResponse));
    }

    public ResponseEntity<BaseResponse<CodeNoteEntry>> addPrNotesByPosition(AddPrCodeNoteRequest request) {
        return handleServiceOperation(() -> manager.addPrNotesByPosition(request), "Added successfully", null, null);
    }
}
