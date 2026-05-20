package com.authserver.authserver.expense_tracker.controllers;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.expense_tracker.entry.MonthlyAnalysisResponse;
import com.authserver.authserver.expense_tracker.services.AnalyticsService;

@RestController
@RequestMapping("/expense-tracker/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * GET /expense-tracker/analytics/monthly?month=2026-05
     *
     * Returns summary, category breakdown, and daily trend for the
     * authenticated user in the specified month (YYYY-MM).
     */
    @GetMapping("/monthly")
    public ResponseEntity<BaseResponse<MonthlyAnalysisResponse>> getMonthlyAnalysis(
            @RequestParam String month) {
        try {
            YearMonth yearMonth = YearMonth.parse(month);
            return analyticsService.getMonthlyAnalysis(yearMonth);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid month format. Expected YYYY-MM, got: " + month);
        }
    }
}
