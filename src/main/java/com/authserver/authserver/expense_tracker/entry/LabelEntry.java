package com.authserver.authserver.expense_tracker.entry;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelEntry {

    private Long id;

    private String labelName;

    private Boolean defaultLabel;

    private Long parentId;

    private Long userId;

    private Integer subCategoryCount;

    public LabelEntry(Long id, String labelName) {
        this.id = id;
        this.labelName = labelName;
    }
}
