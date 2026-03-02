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
public class TicketDltListener {

    private static final Logger log = LoggerFactory.getLogger(TicketDltListener.class);

    private final SeatService seatService;

    public TicketDltListener(SeatService seatService) {
        this.seatService = seatService;
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_REQUESTED_TOPIC + ".DLT",
            groupId = "ticket-dlt-consumer-group",
            properties = "spring.json.value.default.type=com.ticketing.common.event.ReservationRequestedEvent")
    public void onReservationRequestedDlt(ReservationRequestedEvent event) {
        log.error("DLT: Failed to reserve seat after retries: reservationId={}, eventId={}, seat={}",
                event.reservationId(), event.eventId(), event.seatNumber());
        seatService.reserveSeat(Long.parseLong(event.eventId()), event.seatNumber(), event.reservationId());
        log.info("DLT: Seat reserved via DLT recovery: reservationId={}", event.reservationId());
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_CANCELLED_TOPIC + ".DLT",
            groupId = "ticket-dlt-consumer-group",
            properties = "spring.json.value.default.type=com.ticketing.common.event.ReservationCancelledEvent")
    public void onReservationCancelledDlt(ReservationCancelledEvent event) {
        log.error("DLT: Failed to release seat after retries: reservationId={}, eventId={}, seat={}",
                event.reservationId(), event.eventId(), event.seatNumber());
        seatService.releaseSeat(Long.parseLong(event.eventId()), event.seatNumber());
        log.info("DLT: Seat released via DLT recovery: reservationId={}", event.reservationId());
    }
}
