package com.ticketing.ticket.controller;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.dto.EventResponse;
import com.ticketing.ticket.dto.SeatResponse;
import com.ticketing.ticket.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "공연·좌석", description = "공연 생성 및 좌석 조회 API")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @Operation(summary = "공연 생성", description = "공연을 등록하고 좌석을 자동 생성한다.")
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        return ResponseEntity.ok(eventService.createEvent(request));
    }

    @GetMapping
    @Operation(summary = "전체 공연 목록 조회")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "공연 단건 조회")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEvent(eventId));
    }

    @GetMapping("/{eventId}/seats")
    @Operation(summary = "전체 좌석 조회", description = "해당 공연의 모든 좌석(예약 여부 포함)을 반환한다.")
    public ResponseEntity<List<SeatResponse>> getAllSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAllSeats(eventId));
    }

    @GetMapping("/{eventId}/seats/available")
    @Operation(summary = "잔여 좌석 조회", description = "예약 가능한 좌석만 반환한다.")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getAvailableSeats(eventId));
    }
}
