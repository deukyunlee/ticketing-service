package com.ticketing.common.event;

import java.time.LocalDateTime;

public record ReservationRequestedEvent (

    String reservationId,
    String userId,
    String eventId,
    String seatNumber,
    long price,
    LocalDateTime createdAt
){}
