package com.authserver.authserver.expense_tracker.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.controllers.BaseController;
import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.expense_tracker.entry.LabelEntry;
import com.authserver.authserver.expense_tracker.services.LabelService;

@RestController
@RequestMapping("/expense-tracker/label")
public class LabelController extends BaseController<Long, LabelEntry, LabelService> {

    @GetMapping("/getAll/user")
    public ResponseEntity<BaseResponse<List<LabelEntry>>> getAllByUser(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return service.getAllByUser(page, size);
    }

}
