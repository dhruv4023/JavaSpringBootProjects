package com.authserver.authserver.expense_tracker.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.authserver.authserver.base.helper.PageResponse;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.response.ResponseBuilder;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.expense_tracker.entry.TransactionEntry;
import com.authserver.authserver.expense_tracker.manager.TransactionManager;

@Service
public class TransactionService extends BaseService<Long, TransactionEntry, TransactionManager> {

    public TransactionService(TransactionManager manager) {
        super(manager);
    }

    public ResponseEntity<BaseResponse<List<TransactionEntry>>> getAllByUser(long page, long size) {
        Page<TransactionEntry> result = manager.getByUserId(page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return ResponseBuilder.list(() -> result.getContent(), "Fetched successfully", null, pageResponse);
    }

    public ResponseEntity<BaseResponse<List<TransactionEntry>>> getAllByLabel(Long labelId, long page, long size) {
        Page<TransactionEntry> result = manager.getByUserIdAndLabelId(labelId, page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return ResponseBuilder.list(() -> result.getContent(), "Fetched successfully", null, pageResponse);
    }

}
