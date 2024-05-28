package com.assignment.api.exception;

public class ParameterErrorException extends RuntimeException {
    public ParameterErrorException(String message) {
        super(message);
    }
}
