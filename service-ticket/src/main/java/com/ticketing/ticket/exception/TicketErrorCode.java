package com.ticketing.ticket.exception;

import com.ticketing.common.exception.ErrorCode;

public enum TicketErrorCode implements ErrorCode {

    // Event 1000~1099
    EVENT_NOT_FOUND(1000, 404, "Event not found"),

    // Seat 1100~1199
    SEAT_NOT_FOUND(1100, 404, "Seat not found"),
    SEAT_ALREADY_RESERVED(1101, 409, "Seat already reserved"),
    SEAT_LOCK_ACQUISITION_FAILED(1102, 423, "Failed to acquire seat lock");

    private final int code;
    private final int status;
    private final String message;

    TicketErrorCode(int code, int status, String message) {
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
