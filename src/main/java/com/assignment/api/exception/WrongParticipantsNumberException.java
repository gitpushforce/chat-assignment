package com.assignment.api.exception;

public class WrongParticipantsNumberException extends RuntimeException {
    public WrongParticipantsNumberException(String message) {
        super(message);
    }
}
