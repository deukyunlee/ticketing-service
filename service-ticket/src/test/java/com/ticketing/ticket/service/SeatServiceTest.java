package com.ticketing.ticket.service;

import com.ticketing.common.exception.BusinessException;
import com.ticketing.ticket.entity.Seat;
import com.ticketing.ticket.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

        seatService.reserveSeat(1L, "A1");

        verify(seatRepository).save(seat);
    }

    @Test
    void reserveSeat_alreadyReserved_shouldThrow() {
        Seat seat = new Seat(1L, "A1");
        seat.markReserved();
        given(seatRepository.findByEventIdAndSeatNumber(1L, "A1")).willReturn(Optional.of(seat));

        assertThatThrownBy(() -> seatService.reserveSeat(1L, "A1"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already reserved");
    }

    @Test
    void releaseSeat_shouldMarkSeatAsAvailable() {
        Seat seat = new Seat(1L, "A1");
        seat.markReserved();
        given(seatRepository.findByEventIdAndSeatNumber(1L, "A1")).willReturn(Optional.of(seat));

        seatService.releaseSeat(1L, "A1");

        verify(seatRepository).save(seat);
    }
}
