package com.authserver.authserver.expense_tracker.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.authserver.authserver.base.BaseRepository;
import com.authserver.authserver.expense_tracker.entry.CategoryBreakdownDTO;
import com.authserver.authserver.expense_tracker.entry.DailyTrendDTO;
import com.authserver.authserver.expense_tracker.entry.MonthlySummaryDTO;
import com.authserver.authserver.expense_tracker.models.TransactionModel;

@Repository
public interface AnalyticsRepository extends BaseRepository<TransactionModel, Long> {

    // Total expense + transaction count for a user within a date range
    @Query("""
            SELECT COALESCE(SUM(t.amt), 0.0) AS totalExpense,
                   COUNT(t.id)              AS transactionCount
            FROM TransactionModel t
            WHERE t.user.id = :userId
              AND t.date >= :start
              AND t.date < :end
            """)
    MonthlySummaryDTO getMonthlySummary(
            @Param("userId") Long userId,
            @Param("start") Instant start,
            @Param("end") Instant end);

    // Category-wise SUM grouped by label
    @Query("""
            SELECT t.label.id      AS labelId,
                   t.label.labelName AS labelName,
                   SUM(t.amt)      AS total
            FROM TransactionModel t
            WHERE t.user.id = :userId
              AND t.date >= :start
              AND t.date < :end
            GROUP BY t.label.id, t.label.labelName
            ORDER BY total DESC
            """)
    List<CategoryBreakdownDTO> getCategoryBreakdown(
            @Param("userId") Long userId,
            @Param("start") Instant start,
            @Param("end") Instant end);

    // Daily SUM – cast Instant date column to LocalDate via FUNCTION
    @Query("""
            SELECT CAST(t.date AS LocalDate) AS date,
                   SUM(t.amt)                AS total
            FROM TransactionModel t
            WHERE t.user.id = :userId
              AND t.date >= :start
              AND t.date < :end
            GROUP BY CAST(t.date AS LocalDate)
            ORDER BY date ASC
            """)
    List<DailyTrendDTO> getDailyTrend(
            @Param("userId") Long userId,
            @Param("start") Instant start,
            @Param("end") Instant end);
}
