package com.ticketing.common.event;

import java.time.LocalDateTime;

public record PaymentFailedEvent(

    String reservationId,
    String userId,
    String reason,
    LocalDateTime failedAt
) {
}
