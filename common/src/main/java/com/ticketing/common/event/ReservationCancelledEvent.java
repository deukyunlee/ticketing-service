package com.ticketing.common.event;

import java.time.LocalDateTime;

public record ReservationCancelledEvent (

    String reservationId,
    String userId,
    String eventId,
    String seatNumber,
    String reason,
    LocalDateTime cancelledAt
){}
