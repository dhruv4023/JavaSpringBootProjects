package com.authserver.authserver.expense_tracker.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.expense_tracker.entry.TransactionEntry;
import com.authserver.authserver.expense_tracker.services.TransactionService;

@RestController
@RequestMapping("/expense-tracker/transaction")
public class TransactionController extends BaseController<Long, TransactionEntry, TransactionService> {

    @GetMapping("/getAll/user")
    public ResponseEntity<BaseResponse<List<TransactionEntry>>> getAllByUser(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.getAllByUser(page, size);
    }

    @GetMapping("/getAll/label/{labelId}")
    public ResponseEntity<BaseResponse<List<TransactionEntry>>> getAllByLabel(
            @PathVariable Long labelId,
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.getAllByLabel(labelId, page, size);
    }

}
