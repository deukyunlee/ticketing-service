package com.ticketing.ticket.listener;

import com.ticketing.common.KafkaConstants;
import com.ticketing.common.event.ReservationCancelledEvent;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.ticket.service.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TicketEventListener {

    private static final Logger log = LoggerFactory.getLogger(TicketEventListener.class);

    private final SeatService seatService;
    
    public TicketEventListener(SeatService seatService) {
        this.seatService = seatService;
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_REQUESTED_TOPIC,
        properties = "spring.json.value.default.type=com.ticketing.common.event.ReservationRequestedEvent")
    public void onReservationRequested(ReservationRequestedEvent event) {
        log.info("Received reservation request: reservationId={}, eventId={}, seat={}",
            event.reservationId(), event.eventId(), event.seatNumber());
        seatService.reserveSeat(Long.parseLong(event.eventId()), event.seatNumber(), event.reservationId());
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_CANCELLED_TOPIC,
        properties = "spring.json.value.default.type=com.ticketing.common.event.ReservationCancelledEvent")
    public void onReservationCancelled(ReservationCancelledEvent event) {
        log.info("Received reservation cancellation: reservationId={}, eventId={}, seat={}",
            event.reservationId(), event.eventId(), event.seatNumber());
        seatService.releaseSeat(Long.parseLong(event.eventId()), event.seatNumber());
    }
}
