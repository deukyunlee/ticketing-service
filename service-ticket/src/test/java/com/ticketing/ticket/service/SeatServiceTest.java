package com.ticketing.ticket.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.ticketing.common.exception.BusinessException;
import com.ticketing.ticket.entity.Seat;
import com.ticketing.ticket.repository.SeatRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private SeatService seatService;

    @Test
    void reserveSeat_shouldMarkSeatAsReserved() {
        Seat seat = new Seat(1L, "A1");
        given(seatRepository.findByEventIdAndSeatNumber(1L, "A1")).willReturn(Optional.of(seat));

        seatService.reserveSeat(1L, "A1", "res-1");

        verify(seatRepository).save(seat);
        assertThat(seat.getReservationId()).isEqualTo("res-1");
    }

    @Test
    void reserveSeat_sameReservation_shouldSkip() {
        Seat seat = new Seat(1L, "A1");
        seat.markReserved("res-1");
        given(seatRepository.findByEventIdAndSeatNumber(1L, "A1")).willReturn(Optional.of(seat));

        seatService.reserveSeat(1L, "A1", "res-1");

        verify(seatRepository, never()).save(seat);
    }

    @Test
    void reserveSeat_differentReservation_shouldThrow() {
        Seat seat = new Seat(1L, "A1");
        seat.markReserved("res-1");
        given(seatRepository.findByEventIdAndSeatNumber(1L, "A1")).willReturn(Optional.of(seat));

        assertThatThrownBy(() -> seatService.reserveSeat(1L, "A1", "res-2"))
            .isInstanceOf(BusinessException.class)
            .hasMessageContaining("already reserved");
    }

    @Test
    void releaseSeat_shouldMarkSeatAsAvailable() {
        Seat seat = new Seat(1L, "A1");
        seat.markReserved("res-1");
        given(seatRepository.findByEventIdAndSeatNumber(1L, "A1")).willReturn(Optional.of(seat));

        seatService.releaseSeat(1L, "A1");

        verify(seatRepository).save(seat);
        assertThat(seat.getReservationId()).isNull();
    }
}
