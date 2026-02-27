package com.ticketing.ticket.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateEventRequest {

    private String title;
    private String description;
    private String venue;
    private LocalDateTime eventDate;
    private int totalSeats;
    private long price;
}
