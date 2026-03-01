package com.ticketing.ticket.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CreateEventRequest {

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String venue;

    @NotNull
    @Future
    private LocalDateTime eventDate;

    @Min(1)
    @Max(1000)
    private int totalSeats;

    @Min(0)
    private long price;
}
