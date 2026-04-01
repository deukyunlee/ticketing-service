package com.ticketing.ticket.service;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.dto.EventResponse;
import com.ticketing.ticket.dto.SeatResponse;
import com.ticketing.ticket.entity.Event;
import com.ticketing.ticket.entity.Seat;
import com.ticketing.common.exception.BusinessException;
import com.ticketing.ticket.exception.TicketErrorCode;
import com.ticketing.ticket.repository.EventRepository;
import com.ticketing.ticket.repository.SeatRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SeatRepository seatRepository;

    public EventService(EventRepository eventRepository, SeatRepository seatRepository) {
        this.eventRepository = eventRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    @CacheEvict(cacheNames = "events-list", allEntries = true)
    public EventResponse createEvent(CreateEventRequest request) {
        Event event = new Event(
                request.getTitle(),
                request.getDescription(),
                request.getVenue(),
                request.getEventDate(),
                request.getTotalSeats(),
                request.getPrice()
        );
        Event saved = eventRepository.save(event);

        List<Seat> seats = IntStream.rangeClosed(1, request.getTotalSeats())
                .mapToObj(i -> new Seat(saved.getId(), "A" + i))
                .toList();
        seatRepository.saveAll(seats);

        return EventResponse.from(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "events-list", key = "'all'")
    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(EventResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "events", key = "#eventId")
    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(TicketErrorCode.EVENT_NOT_FOUND, String.valueOf(eventId)));
        return EventResponse.from(event);
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "available-seats", key = "#eventId")
    public List<SeatResponse> getAvailableSeats(Long eventId) {
        validateEventExists(eventId);
        return seatRepository.findByEventIdAndReserved(eventId, false).stream()
                .map(SeatResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SeatResponse> getAllSeats(Long eventId) {
        validateEventExists(eventId);
        return seatRepository.findByEventId(eventId).stream()
                .map(SeatResponse::from)
                .toList();
    }

    private void validateEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new BusinessException(TicketErrorCode.EVENT_NOT_FOUND, String.valueOf(eventId));
        }
    }
}
