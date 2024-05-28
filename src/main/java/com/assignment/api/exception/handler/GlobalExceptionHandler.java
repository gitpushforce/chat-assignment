package com.assignment.api.exception.handler;

import com.assignment.api.dto.response.ErrorDto;
import com.assignment.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WrongParticipantsNumberException.class)
    public ResponseEntity<?> WrongParticipantsNumberExceptionHandling(WrongParticipantsNumberException exception){
        ErrorDto errorDetails = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatCreationException.class)
    public ResponseEntity<?> ChatCreationExceptionHandling(ChatCreationException exception){
        ErrorDto errorDetails = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<?> ChatNotFoundExceptionHandling(ChatNotFoundException exception){
        ErrorDto errorDetails = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> UserNotFoundExceptionHandling(UserNotFoundException exception){
        ErrorDto errorDetails = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<?> MessageNotFoundExceptionHandling(MessageNotFoundException exception){
        ErrorDto errorDetails = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterErrorException.class)
    public ResponseEntity<?> ParameterErrorExceptionHandling(ParameterErrorException exception){
        ErrorDto errorDetails = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
