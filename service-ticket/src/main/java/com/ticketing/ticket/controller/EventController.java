package com.ticketing.ticket.controller;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.entity.Event;
import com.ticketing.ticket.entity.Seat;
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
    public ResponseEntity<Event> createEvent(@RequestBody CreateEventRequest request) {
        Event event = eventService.createEvent(request);
        return ResponseEntity.ok(event);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @GetMapping("/{eventId}/seats")
    public ResponseEntity<List<Seat>> getAllSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAllSeats(eventId));
    }

    @GetMapping("/{eventId}/seats/available")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAvailableSeats(eventId));
    }
}
