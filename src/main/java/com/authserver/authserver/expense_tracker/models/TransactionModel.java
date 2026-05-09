package com.authserver.authserver.expense_tracker.models;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "expense_transactions", indexes = {
        // Covers monthly analytics: filter by user + month range
        @Index(name = "idx_txn_user_date", columnList = "user_id, date"),
        // Covers category breakdown: filter by user + label + date
        @Index(name = "idx_txn_user_label_date", columnList = "user_id, label_id, date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class TransactionModel extends BaseModel {

    @Column(name = "comment")
    private String comment;

    @Column(name = "amt", nullable = false)
    private Double amt;

    @Column(name = "date", nullable = false)
    private Instant date;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne()
    @JoinColumn(name = "label_id", nullable = false)
    private LabelModel label;

}
