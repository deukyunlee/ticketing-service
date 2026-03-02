package com.ticketing.common.event;

import java.time.LocalDateTime;

public record ReservationConfirmedEvent (

    String reservationId,
    String userId,
    String eventId,
    String seatNumber,
    LocalDateTime confirmedAt
){}
