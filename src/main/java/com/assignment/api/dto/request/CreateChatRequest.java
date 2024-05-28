package com.assignment.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateChatRequest {
    private List<Long> participantsIds;
    private Boolean isGroup;
}
