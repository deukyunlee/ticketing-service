package com.ticketing.ticket.service;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.entity.Event;
import com.ticketing.ticket.entity.Seat;
import com.ticketing.common.exception.BusinessException;
import com.ticketing.ticket.exception.TicketErrorCode;
import com.ticketing.ticket.repository.EventRepository;
import com.ticketing.ticket.repository.SeatRepository;
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
    public Event createEvent(CreateEventRequest request) {
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

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException(TicketErrorCode.EVENT_NOT_FOUND, String.valueOf(eventId)));
    }

    @Transactional(readOnly = true)
    public List<Seat> getAvailableSeats(Long eventId) {
        validateEventExists(eventId);
        return seatRepository.findByEventIdAndReserved(eventId, false);
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllSeats(Long eventId) {
        validateEventExists(eventId);
        return seatRepository.findByEventId(eventId);
    }

    private void validateEventExists(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new BusinessException(TicketErrorCode.EVENT_NOT_FOUND, String.valueOf(eventId));
        }
    }
}
