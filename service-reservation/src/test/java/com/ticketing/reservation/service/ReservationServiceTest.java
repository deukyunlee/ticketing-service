package com.ticketing.reservation.service;

import com.ticketing.common.ReservationStatus;
import com.ticketing.common.event.ReservationCancelledEvent;
import com.ticketing.common.event.ReservationConfirmedEvent;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.reservation.dto.ReservationRequest;
import com.ticketing.reservation.dto.ReservationResponse;
import com.ticketing.reservation.entity.Reservation;
import com.ticketing.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void createReservation_shouldSaveAndPublishEvent() {
        ReservationRequest request = new ReservationRequest("user-1", "1", "A1", 50000);
        ArgumentCaptor<Reservation> reservationCaptor = ArgumentCaptor.forClass(Reservation.class);
        ArgumentCaptor<ReservationRequestedEvent> eventCaptor = ArgumentCaptor.forClass(ReservationRequestedEvent.class);

        ReservationResponse result = reservationService.createReservation(request);

        assertThat(result.userId()).isEqualTo("user-1");
        assertThat(result.eventId()).isEqualTo("1");
        assertThat(result.status()).isEqualTo(ReservationStatus.PENDING);

        verify(reservationRepository).save(reservationCaptor.capture());
        verify(eventPublisher).publishEvent(eventCaptor.capture());

        Reservation saved = reservationCaptor.getValue();
        ReservationRequestedEvent published = eventCaptor.getValue();
        assertThat(published.reservationId()).isEqualTo(saved.getId());
        assertThat(published.userId()).isEqualTo("user-1");
        assertThat(published.eventId()).isEqualTo("1");
        assertThat(published.seatNumber()).isEqualTo("A1");
        assertThat(published.price()).isEqualTo(50000);
    }

    @Test
    void confirmReservation_shouldUpdateStatusAndPublishEvent() {
        Reservation reservation = new Reservation("res-1", "user-1", "1", "A1", 50000);
        given(reservationRepository.findById("res-1")).willReturn(Optional.of(reservation));

        reservationService.confirmReservation("res-1");

        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        verify(reservationRepository).save(reservation);

        ArgumentCaptor<ReservationConfirmedEvent> eventCaptor = ArgumentCaptor.forClass(ReservationConfirmedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().reservationId()).isEqualTo("res-1");
    }

    @Test
    void cancelReservation_shouldUpdateStatusAndPublishEvent() {
        Reservation reservation = new Reservation("res-1", "user-1", "1", "A1", 50000);
        given(reservationRepository.findById("res-1")).willReturn(Optional.of(reservation));

        reservationService.cancelReservation("res-1", "Payment failed");

        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
        verify(reservationRepository).save(reservation);

        ArgumentCaptor<ReservationCancelledEvent> eventCaptor = ArgumentCaptor.forClass(ReservationCancelledEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().reservationId()).isEqualTo("res-1");
        assertThat(eventCaptor.getValue().reason()).isEqualTo("Payment failed");
    }
}
