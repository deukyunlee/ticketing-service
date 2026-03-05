package com.ticketing.reservation.listener;

import com.ticketing.common.KafkaConstants;
import com.ticketing.common.event.PaymentCompletedEvent;
import com.ticketing.common.event.PaymentFailedEvent;
import com.ticketing.reservation.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationDltListener {

    private static final Logger log = LoggerFactory.getLogger(ReservationDltListener.class);

    private final ReservationService reservationService;

    public ReservationDltListener(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = KafkaConstants.PAYMENT_COMPLETED_TOPIC + ".DLT",
        groupId = "${spring.kafka.consumer.group-id}-dlt",
        properties = "spring.json.value.default.type=com.ticketing.common.event.PaymentCompletedEvent")
    public void onPaymentCompletedDlt(PaymentCompletedEvent event) {
        log.error("DLT: Failed to confirm reservation after retries: reservationId={}", event.reservationId());
        reservationService.confirmReservation(event.reservationId());
        log.info("DLT: Reservation confirmed via DLT recovery: reservationId={}", event.reservationId());
    }

    @KafkaListener(topics = KafkaConstants.PAYMENT_FAILED_TOPIC + ".DLT",
        groupId = "${spring.kafka.consumer.group-id}-dlt",
        properties = "spring.json.value.default.type=com.ticketing.common.event.PaymentFailedEvent")
    public void onPaymentFailedDlt(PaymentFailedEvent event) {
        log.error("DLT: Failed to cancel reservation after retries: reservationId={}", event.reservationId());
        reservationService.cancelReservation(event.reservationId(), event.reason());
        log.info("DLT: Reservation cancelled via DLT recovery: reservationId={}", event.reservationId());
    }
}
