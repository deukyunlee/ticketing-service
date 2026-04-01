package com.ticketing.ticket.dto;

import com.ticketing.ticket.entity.Event;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "공연 정보")
public record EventResponse(
        @Schema(description = "공연 ID") Long id,
        @Schema(description = "제목") String title,
        @Schema(description = "설명") String description,
        @Schema(description = "공연장") String venue,
        @Schema(description = "공연 일시") LocalDateTime eventDate,
        @Schema(description = "총 좌석 수") int totalSeats,
        @Schema(description = "좌석당 가격(원)") long price,
        @Schema(description = "생성 시각") LocalDateTime createdAt
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getVenue(),
                event.getEventDate(),
                event.getTotalSeats(),
                event.getPrice(),
                event.getCreatedAt()
        );
    }
}
