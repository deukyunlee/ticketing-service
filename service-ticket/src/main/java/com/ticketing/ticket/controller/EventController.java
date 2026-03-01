package com.ticketing.ticket.controller;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.dto.EventResponse;
import com.ticketing.ticket.dto.SeatResponse;
import com.ticketing.ticket.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<SeatResponse>> getAllSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAllSeats(eventId));
    }

    @GetMapping("/{eventId}/seats/available")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAvailableSeats(eventId));
    }
}
