package com.assignment.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CHAT_TBL")
@Getter
@Setter
public class ChatTbl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IS_GROUP")
    private Boolean isGroup;
}
