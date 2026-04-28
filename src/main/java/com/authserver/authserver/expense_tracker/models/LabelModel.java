package com.authserver.authserver.expense_tracker.models;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expense_labels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class LabelModel extends BaseModel {

    @Column(name = "label_name", nullable = false)
    private String labelName;

    @Column(name = "default_label", nullable = false)
    private boolean defaultLabel = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserModel user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private LabelModel parent;

}
