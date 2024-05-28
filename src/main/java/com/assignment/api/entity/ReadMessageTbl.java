package com.assignment.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "READ_MESSAGE_TBL")
@Getter
@Setter
public class ReadMessageTbl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    private MessageTbl message;

    @ManyToOne
    @JoinColumn(name = "reader_id", nullable = false)
    private UserMst reader;

    @Column(name = "read_date")
    private LocalDateTime readDate;
}
