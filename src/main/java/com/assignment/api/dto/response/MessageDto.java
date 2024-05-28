package com.assignment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MessageDto {
    private Long messageId;
    private Long senderId;
    private String senderName;
    private String content;
    private Integer readCount;
    private String readStatus;
}
