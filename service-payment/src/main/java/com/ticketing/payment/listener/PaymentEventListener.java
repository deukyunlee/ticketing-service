package com.ticketing.payment.listener;

import com.ticketing.common.KafkaConstants;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    private final PaymentService paymentService;

    public PaymentEventListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_REQUESTED_TOPIC,
        properties = "spring.json.value.default.type=com.ticketing.common.event.ReservationRequestedEvent")
    public void onReservationRequested(ReservationRequestedEvent event) {
        log.info("Received reservation request for payment: reservationId={}, amount={}",
            event.reservationId(), event.price());
        paymentService.processPayment(event);
    }
}
