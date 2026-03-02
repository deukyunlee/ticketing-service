package com.ticketing.reservation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReservationRequest(

    @NotBlank
    String userId,

    @NotBlank
    String eventId,

    @NotBlank
    String seatNumber,

    @Min(0)
    long price
) {
}