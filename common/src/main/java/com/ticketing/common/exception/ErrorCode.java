package com.ticketing.common.exception;

public interface ErrorCode {
    int getCode();
    int getStatus();
    String getMessage();
    String name();
}
