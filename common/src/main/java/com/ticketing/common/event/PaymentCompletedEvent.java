package com.ticketing.common.event;

import java.time.LocalDateTime;

public record PaymentCompletedEvent (

    String paymentId,
    String reservationId,
    String userId,
    long amount,
    LocalDateTime paidAt
){}
