package com.authserver.authserver.chat_app.models;

import java.util.List;

import com.authserver.authserver.base.BaseModel;
import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "conversations")
public class Conversations extends BaseModel {

    @Column(unique = true, nullable = false)
    private String conversationId;

    @ManyToMany
    @JoinTable(
        name = "conversation_participants",
        joinColumns = @JoinColumn(name = "conversation_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserModel> participants;
}