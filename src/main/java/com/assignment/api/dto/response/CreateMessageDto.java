package com.assignment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateMessageDto {
    private Long messageId;
    private Long senderId;
    private String senderName;
    private String content;
    private Boolean success;
}
