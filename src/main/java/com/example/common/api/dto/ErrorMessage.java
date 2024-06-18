package com.example.common.api.dto;

import java.time.LocalDateTime;

public class ErrorMessage {
    private int statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ErrorMessage() {
    }

    public ErrorMessage(int statusCode, String message, LocalDateTime timestamp) {
        this.statusCode = statusCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
