package com.ticketing.payment.listener;

import com.ticketing.common.KafkaConstants;
import com.ticketing.common.event.PaymentFailedEvent;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.payment.entity.Payment;
import com.ticketing.payment.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PaymentDltListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentDltListener.class);

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentDltListener(PaymentRepository paymentRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = KafkaConstants.RESERVATION_REQUESTED_TOPIC + ".DLT",
        groupId = "${spring.kafka.consumer.group-id}-dlt",
        properties = "spring.json.value.default.type=com.ticketing.common.event.ReservationRequestedEvent")
    @Transactional
    public void onReservationRequestedDlt(ReservationRequestedEvent event) {
        log.error("DLT: Processing failed reservation-requested: reservationId={}", event.reservationId());

        if (paymentRepository.findByReservationId(event.reservationId()).isPresent()) {
            log.warn("DLT: Payment already exists for reservationId={}, skipping", event.reservationId());
            return;
        }

        String paymentId = UUID.randomUUID().toString();
        Payment payment = new Payment(paymentId, event.reservationId(), event.userId(), event.price());
        payment.markFailed("Payment processing failed after retries");
        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentFailedEvent(
            event.reservationId(), event.userId(),
            "Payment processing failed after retries", LocalDateTime.now()
        ));
        log.info("DLT: Payment marked as FAILED and event published: reservationId={}", event.reservationId());
    }
}
