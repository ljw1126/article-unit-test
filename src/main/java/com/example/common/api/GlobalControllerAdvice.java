package com.example.common.api;

import com.example.common.api.dto.ErrorMessage;
import com.example.common.exception.AccessDeniedException;
import com.example.common.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorMessage handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ErrorMessage handleAccessDeniedException(AccessDeniedException exception) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(), exception.getMessage(), LocalDateTime.now());
    }
}
