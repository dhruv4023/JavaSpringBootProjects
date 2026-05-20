package com.authserver.authserver.expense_tracker.entry;

import java.time.LocalDate;

public interface DailyTrendDTO {
    LocalDate getDate();
    Double getTotal();
}
