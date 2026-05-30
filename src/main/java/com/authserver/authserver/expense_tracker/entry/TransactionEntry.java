package com.authserver.authserver.expense_tracker.entry;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntry {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uuid;

    private String comment;

    private LabelEntry label;

    private Double amt;

    private Instant date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID userUuid;

}
