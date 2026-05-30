package com.authserver.authserver.expense_tracker.entry;

import java.util.UUID;

public interface CategoryBreakdownDTO {
    UUID getLabelUuid();
    String getLabelName();
    Double getTotal();
}
