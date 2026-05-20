package com.authserver.authserver.expense_tracker.services;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.authserver.authserver.base.response.BaseResponse;
import com.authserver.authserver.base.response.ResponseBuilder;
import com.authserver.authserver.expense_tracker.entry.CategoryBreakdownDTO;
import com.authserver.authserver.expense_tracker.entry.DailyTrendDTO;
import com.authserver.authserver.expense_tracker.entry.MonthlyAnalysisResponse;
import com.authserver.authserver.expense_tracker.entry.MonthlySummaryDTO;
import com.authserver.authserver.expense_tracker.repositories.AnalyticsRepository;
import com.authserver.authserver.user.util.SecurityUtils;

@Service
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final SecurityUtils securityUtils;

    public AnalyticsService(AnalyticsRepository analyticsRepository, SecurityUtils securityUtils) {
        this.analyticsRepository = analyticsRepository;
        this.securityUtils = securityUtils;
    }

    public ResponseEntity<BaseResponse<MonthlyAnalysisResponse>> getMonthlyAnalysis(YearMonth yearMonth) {
        Long userId = securityUtils.getCurrentUserId();

        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end   = yearMonth.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        int daysInMonth = yearMonth.lengthOfMonth();

        // --- Summary ---
        MonthlySummaryDTO summaryDTO = analyticsRepository.getMonthlySummary(userId, start, end);
        double totalExpense     = summaryDTO.getTotalExpense() != null ? summaryDTO.getTotalExpense() : 0.0;
        long   transactionCount = summaryDTO.getTransactionCount() != null ? summaryDTO.getTransactionCount() : 0L;
        double avgDailyExpense  = daysInMonth > 0 ? totalExpense / daysInMonth : 0.0;

        MonthlyAnalysisResponse.Summary summary = new MonthlyAnalysisResponse.Summary(
                totalExpense, transactionCount, avgDailyExpense);

        // --- Category breakdown ---
        List<CategoryBreakdownDTO> categoryDTOs = analyticsRepository.getCategoryBreakdown(userId, start, end);
        List<MonthlyAnalysisResponse.CategoryBreakdown> categoryBreakdown = categoryDTOs.stream()
                .map(dto -> new MonthlyAnalysisResponse.CategoryBreakdown(
                        dto.getLabelId(), dto.getLabelName(), dto.getTotal()))
                .collect(Collectors.toList());

        // --- Daily trend ---
        List<DailyTrendDTO> dailyDTOs = analyticsRepository.getDailyTrend(userId, start, end);
        List<MonthlyAnalysisResponse.DailyTrend> dailyTrend = dailyDTOs.stream()
                .map(dto -> new MonthlyAnalysisResponse.DailyTrend(
                        dto.getDate().toString(), dto.getTotal()))
                .collect(Collectors.toList());

        MonthlyAnalysisResponse response = new MonthlyAnalysisResponse(
                yearMonth.toString(), summary, categoryBreakdown, dailyTrend);

        return ResponseBuilder.single(() -> response, "Monthly analysis fetched successfully", null);
    }
}
