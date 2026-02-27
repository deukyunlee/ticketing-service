package com.ticketing.ticket.service;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.entity.Event;
import com.ticketing.ticket.entity.Seat;
import com.ticketing.ticket.repository.EventRepository;
import com.ticketing.ticket.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEvent_shouldSaveEventAndCreateSeats() {
        CreateEventRequest request = new CreateEventRequest(
                "Concert", "Great show", "Hall A",
                LocalDateTime.of(2026, 6, 1, 19, 0), 3, 50000
        );

        Event savedEvent = new Event("Concert", "Great show", "Hall A",
                LocalDateTime.of(2026, 6, 1, 19, 0), 3, 50000);
        given(eventRepository.save(any(Event.class))).willReturn(savedEvent);

        eventService.createEvent(request);

        verify(eventRepository).save(any(Event.class));
        ArgumentCaptor<List<Seat>> seatsCaptor = ArgumentCaptor.forClass(List.class);
        verify(seatRepository).saveAll(seatsCaptor.capture());
        assertThat(seatsCaptor.getValue()).hasSize(3);
    }
}
