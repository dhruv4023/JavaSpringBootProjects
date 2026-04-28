package com.authserver.authserver.expense_tracker.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.authserver.authserver.base.helper.PageResponse;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.response.ResponseBuilder;
import com.authserver.authserver.base.service.BaseService;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.manager.LabelManager;

@Service
public class LabelService extends BaseService<Long, LabelEntry, LabelManager> {

    public LabelService(LabelManager manager) {
        super(manager);
    }

    public ResponseEntity<BaseResponse<List<LabelEntry>>> getAllByUser(long page, long size) {
        Page<LabelEntry> result = manager.getByUserId(page, size);
        PageResponse pageResponse = new PageResponse(page, size, result.getTotalElements());
        return ResponseBuilder.list(() -> result.getContent(), "Fetched successfully", null, pageResponse);
    }

}
