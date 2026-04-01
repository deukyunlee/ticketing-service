package com.ticketing.ticket.dto;

import com.ticketing.ticket.entity.Seat;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "좌석 정보")
public record SeatResponse(
        @Schema(description = "좌석 ID") Long id,
        @Schema(description = "공연 ID") Long eventId,
        @Schema(description = "좌석 번호", example = "A1") String seatNumber,
        @Schema(description = "예약 여부") boolean reserved
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
