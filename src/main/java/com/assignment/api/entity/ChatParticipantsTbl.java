package com.assignment.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CHAT_PARTICIPANTS_TBL")
@IdClass(ChatParticipantsId.class)
@Getter
@Setter
public class ChatParticipantsTbl {
    @Id
    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatTbl chat;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserMst user;
}
