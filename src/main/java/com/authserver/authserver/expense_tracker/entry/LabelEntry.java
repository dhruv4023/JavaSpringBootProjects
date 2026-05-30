package com.authserver.authserver.expense_tracker.entry;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelEntry {

    private UUID uuid;

    private String labelName;

    private Boolean defaultLabel;

    private UUID parentUuid;

    private UUID userUuid;

    private Integer subCategoryCount;

    public LabelEntry(UUID uuid, String labelName) {
        this.uuid = uuid;
        this.labelName = labelName;
    }
}
