package com.assignment.api.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatParticipantsId implements Serializable {
    private Long chat;
    private Long user;
}
