package com.ticketing.reservation.dto;

import com.ticketing.common.ReservationStatus;
import com.ticketing.reservation.entity.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "예약 정보")
public record ReservationResponse(
        @Schema(description = "예약 ID") String id,
        @Schema(description = "사용자 ID") String userId,
        @Schema(description = "공연 ID") String eventId,
        @Schema(description = "좌석 번호") String seatNumber,
        @Schema(description = "금액(원)") long price,
        @Schema(description = "상태 (PENDING, CONFIRMED, CANCELLED 등)") ReservationStatus status,
        @Schema(description = "생성 시각") LocalDateTime createdAt,
        @Schema(description = "수정 시각") LocalDateTime updatedAt
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getEventId(),
                reservation.getSeatNumber(),
                reservation.getPrice(),
                reservation.getStatus(),
                reservation.getCreatedAt(),
                reservation.getUpdatedAt()
        );
    }
}
