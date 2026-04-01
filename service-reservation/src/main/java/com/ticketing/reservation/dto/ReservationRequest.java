package com.ticketing.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "예약 요청")
public record ReservationRequest(
    @NotBlank
    @Schema(description = "공연 ID (Ticket 서비스의 event id)", example = "1")
    String eventId,

    @NotBlank
    @Schema(description = "좌석 번호", example = "A12")
    String seatNumber,

    @Min(0)
    @Schema(description = "결제 예정 금액(원)", example = "50000")
    long price
) {
}