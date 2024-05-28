package com.assignment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PushSettingsDto {
    private Long userId;
    private List<String> notification;
    private Boolean success;
}
