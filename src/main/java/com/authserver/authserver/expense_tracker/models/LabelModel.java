package com.authserver.authserver.expense_tracker.models;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expense_labels", indexes = {
        @Index(name = "idx_label_name", columnList = "label_name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)

public class LabelModel extends BaseModel {

    @Column(name = "label_name", nullable = false)
    private String labelName;

    @Column(name = "default_label", nullable = false)
    private Boolean defaultLabel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_uuid")
    private LabelModel parent;
}
