package com.ticketing.reservation.exception;

import com.ticketing.common.exception.ErrorCode;

public enum ReservationErrorCode implements ErrorCode {

    RESERVATION_NOT_FOUND(2000, 404, "Reservation not found");

    private final int code;
    private final int status;
    private final String message;

    ReservationErrorCode(int code, int status, String message) {
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
