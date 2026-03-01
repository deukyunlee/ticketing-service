package com.ticketing.ticket.dto;

import com.ticketing.ticket.entity.Event;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        String venue,
        LocalDateTime eventDate,
        int totalSeats,
        long price,
        LocalDateTime createdAt
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
