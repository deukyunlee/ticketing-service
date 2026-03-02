package com.ticketing.reservation.service;

import com.ticketing.common.ReservationStatus;
import com.ticketing.common.event.ReservationCancelledEvent;
import com.ticketing.common.event.ReservationConfirmedEvent;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.common.exception.BusinessException;
import com.ticketing.reservation.dto.ReservationRequest;
import com.ticketing.reservation.dto.ReservationResponse;
import com.ticketing.reservation.entity.Reservation;
import com.ticketing.reservation.exception.ReservationErrorCode;
import com.ticketing.reservation.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ReservationService(ReservationRepository reservationRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.reservationRepository = reservationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        String reservationId = UUID.randomUUID().toString();

        Reservation reservation = new Reservation(
                reservationId, request.userId(), request.eventId(),
                request.seatNumber(), request.price()
        );
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationRequestedEvent(
                reservationId, request.userId(), request.eventId(),
                request.seatNumber(), request.price(), LocalDateTime.now()
        ));

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void confirmReservation(String reservationId) {
        Reservation reservation = findReservationById(reservationId);

        reservation.updateStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationConfirmedEvent(
                reservationId, reservation.getUserId(),
                reservation.getEventId(), reservation.getSeatNumber(), LocalDateTime.now()
        ));
        log.info("Reservation confirmed: {}", reservationId);
    }

    @Transactional
    public void cancelReservation(String reservationId, String reason) {
        Reservation reservation = findReservationById(reservationId);

        reservation.updateStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        eventPublisher.publishEvent(new ReservationCancelledEvent(
                reservationId, reservation.getUserId(),
                reservation.getEventId(), reservation.getSeatNumber(), reason, LocalDateTime.now()
        ));
        log.info("Reservation cancelled: id={}, reason={}", reservationId, reason);
    }

    @Transactional(readOnly = true)
    public ReservationResponse getReservation(String reservationId) {
        return ReservationResponse.from(findReservationById(reservationId));
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByUser(String userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private Reservation findReservationById(String reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ReservationErrorCode.RESERVATION_NOT_FOUND, reservationId));
    }
}
