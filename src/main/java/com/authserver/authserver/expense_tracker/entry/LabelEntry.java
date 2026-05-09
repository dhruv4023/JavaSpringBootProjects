package com.authserver.authserver.expense_tracker.entry;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelEntry {

    private Long id;

    private String labelName;

    private Boolean defaultLabel;

    private Long parentId;

    private Long userId;

    public LabelEntry(Long id, String labelName) {
        this.id = id;
        this.labelName = labelName;
    }

}
