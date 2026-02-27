package com.ticketing.common.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReservationCancelledEvent {

    private String reservationId;
    private String userId;
    private String eventId;
    private String seatNumber;
    private String reason;
    private LocalDateTime cancelledAt;
}
