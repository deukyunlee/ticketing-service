package com.ticketing.ticket.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String venue;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private long price;

    private LocalDateTime createdAt;

    public Event(String title, String description, String venue,
        LocalDateTime eventDate, int totalSeats, long price) {
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.eventDate = eventDate;
        this.totalSeats = totalSeats;
        this.price = price;
        this.createdAt = LocalDateTime.now();
    }
}
