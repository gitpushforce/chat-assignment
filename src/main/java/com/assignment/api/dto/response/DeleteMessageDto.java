package com.assignment.api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class DeleteMessageDto {
    private Long deletedMessageId;
    private Boolean success;
}
