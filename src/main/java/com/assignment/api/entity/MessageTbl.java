package com.assignment.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE_TBL")
@Getter
@Setter
public class MessageTbl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private UserMst sender;

    @Column(name = "content", nullable = true)
    private String content;

    @Column(name = "read_flg", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean readFlag;

    @ManyToOne
    @JoinColumn(name = "chat_id", referencedColumnName = "id")
    private ChatTbl chat;

    @Column(name = "update_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;
}
