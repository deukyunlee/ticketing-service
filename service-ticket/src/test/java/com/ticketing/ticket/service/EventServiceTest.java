package com.ticketing.ticket.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ticketing.ticket.dto.CreateEventRequest;
import com.ticketing.ticket.dto.EventResponse;
import com.ticketing.ticket.entity.Event;
import com.ticketing.ticket.entity.Seat;
import com.ticketing.ticket.repository.EventRepository;
import com.ticketing.ticket.repository.SeatRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        EventResponse response = eventService.createEvent(request);

        assertThat(response.title()).isEqualTo("Concert");
        assertThat(response.totalSeats()).isEqualTo(3);
        verify(eventRepository).save(any(Event.class));
        ArgumentCaptor<List<Seat>> seatsCaptor = ArgumentCaptor.forClass(List.class);
        verify(seatRepository).saveAll(seatsCaptor.capture());
        assertThat(seatsCaptor.getValue()).hasSize(3);
    }
}
