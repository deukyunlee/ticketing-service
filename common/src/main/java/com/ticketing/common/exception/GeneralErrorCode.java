package com.ticketing.common.exception;

public enum GeneralErrorCode implements ErrorCode {

    BAD_REQUEST(9000, 400, "Bad request"),
    INTERNAL_SERVER_ERROR(9999, 500, "Internal server error");

    private final int code;
    private final int status;
    private final String message;

    GeneralErrorCode(int code, int status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    @Override
    public int getCode() { return code; }
    @Override
    public int getStatus() { return status; }
    @Override
    public String getMessage() { return message; }
}
