package com.ticketing.ticket.dto;

import com.ticketing.ticket.entity.Seat;

public record SeatResponse(
        Long id,
        Long eventId,
        String seatNumber,
        boolean reserved
) {
    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getEventId(),
                seat.getSeatNumber(),
                seat.isReserved()
        );
    }
}
