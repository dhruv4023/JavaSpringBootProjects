package com.authserver.authserver.expense_tracker.entry;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyAnalysisResponse {

    private String month;
    private Summary summary;
    private List<CategoryBreakdown> categoryBreakdown;
    private List<DailyTrend> dailyTrend;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Double totalExpense;
        private Long transactionCount;
        private Double avgDailyExpense;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryBreakdown {
        private Long labelId;
        private String labelName;
        private Double total;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyTrend {
        private String date;
        private Double total;
    }
}
