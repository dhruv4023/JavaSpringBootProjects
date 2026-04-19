package com.authserver.authserver.chat_app.models;

import java.util.List;

import com.authserver.authserver.user.models.UserModel;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ChatProfile {

    @OneToOne
    private UserModel user;

    @ManyToMany(mappedBy = "participants")
    private List<Conversations> conversations;
}
