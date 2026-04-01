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
public class PaymentResultListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentResultListener.class);

    private final ReservationService reservationService;

    public PaymentResultListener(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @KafkaListener(topics = KafkaConstants.PAYMENT_COMPLETED_TOPIC,
            properties = "spring.json.value.default.type=com.ticketing.common.event.PaymentCompletedEvent")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Payment completed: paymentId={}, reservationId={}",
                event.paymentId(), event.reservationId());
        reservationService.confirmReservation(event.reservationId(), event.userId());
    }

    @KafkaListener(topics = KafkaConstants.PAYMENT_FAILED_TOPIC,
            properties = "spring.json.value.default.type=com.ticketing.common.event.PaymentFailedEvent")
    public void onPaymentFailed(PaymentFailedEvent event) {
        log.info("Payment failed: reservationId={}, reason={}",
                event.reservationId(), event.reason());
        reservationService.cancelReservation(event.reservationId(), event.reason(), event.userId());
    }
}
