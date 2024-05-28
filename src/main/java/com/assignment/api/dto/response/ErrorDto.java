package com.assignment.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDto implements Serializable {
    private Integer status;
    private String message;
    private LocalDateTime timestamp;
}

