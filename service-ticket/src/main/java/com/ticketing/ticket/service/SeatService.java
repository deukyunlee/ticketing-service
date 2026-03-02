package com.ticketing.ticket.service;

import com.ticketing.ticket.entity.Seat;
import com.ticketing.common.exception.BusinessException;
import com.ticketing.ticket.exception.TicketErrorCode;
import com.ticketing.ticket.repository.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeatService {

    private static final Logger log = LoggerFactory.getLogger(SeatService.class);

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Transactional
    public void reserveSeat(Long eventId, String seatNumber, String reservationId) {
        Seat seat = seatRepository.findByEventIdAndSeatNumber(eventId, seatNumber)
                .orElseThrow(() -> new BusinessException(TicketErrorCode.SEAT_NOT_FOUND,
                        "eventId=" + eventId + ", seat=" + seatNumber));

        if (seat.isReserved()) {
            if (reservationId.equals(seat.getReservationId())) {
                log.warn("Seat already reserved by same reservation: eventId={}, seat={}, reservationId={}, skipping",
                        eventId, seatNumber, reservationId);
                return;
            }
            throw new BusinessException(TicketErrorCode.SEAT_ALREADY_RESERVED, seatNumber);
        }

        seat.markReserved(reservationId);
        seatRepository.save(seat);
        log.info("Seat reserved: eventId={}, seat={}, reservationId={}", eventId, seatNumber, reservationId);
    }

    @Transactional
    public void releaseSeat(Long eventId, String seatNumber) {
        Seat seat = seatRepository.findByEventIdAndSeatNumber(eventId, seatNumber)
                .orElseThrow(() -> new BusinessException(TicketErrorCode.SEAT_NOT_FOUND,
                        "eventId=" + eventId + ", seat=" + seatNumber));

        seat.markAvailable();
        seatRepository.save(seat);
        log.info("Seat released: eventId={}, seat={}", eventId, seatNumber);
    }
}
