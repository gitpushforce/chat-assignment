package com.assignment.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMessageRequest {
    private Long senderId;
    private String content;
}
