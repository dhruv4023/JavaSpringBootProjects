package com.authserver.authserver.expense_tracker.entry;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelEntry {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String labelName;

    private Boolean defaultLabel;

    private Long parentId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

}
