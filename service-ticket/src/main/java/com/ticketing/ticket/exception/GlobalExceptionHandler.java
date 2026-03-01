package com.ticketing.ticket.exception;

import com.ticketing.common.exception.BusinessException;
import com.ticketing.common.exception.ErrorCode;
import com.ticketing.common.exception.ErrorResponse;
import com.ticketing.common.exception.GeneralErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[{}] {}", errorCode.name(), e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode, e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("Validation failed: {}", message);
        ErrorCode errorCode = GeneralErrorCode.BAD_REQUEST;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unhandled exception", e);
        ErrorCode errorCode = GeneralErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode, "An internal server error occurred."));
    }
}
