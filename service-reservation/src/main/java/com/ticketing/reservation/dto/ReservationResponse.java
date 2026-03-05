package com.ticketing.reservation.dto;

import com.ticketing.common.ReservationStatus;
import com.ticketing.reservation.entity.Reservation;
import java.time.LocalDateTime;

public record ReservationResponse(
    String id,
    String userId,
    String eventId,
    String seatNumber,
    long price,
    ReservationStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getUserId(),
            reservation.getEventId(),
            reservation.getSeatNumber(),
            reservation.getPrice(),
            reservation.getStatus(),
            reservation.getCreatedAt(),
            reservation.getUpdatedAt()
        );
    }
}
