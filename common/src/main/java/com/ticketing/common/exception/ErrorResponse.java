package com.ticketing.common.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final int errorCode;
    private final String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getStatus();
        this.errorCode = errorCode.getCode();
        this.message = message;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public int getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
}
