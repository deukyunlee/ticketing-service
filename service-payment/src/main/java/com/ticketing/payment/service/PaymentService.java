package com.ticketing.payment.service;

import com.ticketing.common.event.PaymentCompletedEvent;
import com.ticketing.common.event.PaymentFailedEvent;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.payment.entity.Payment;
import com.ticketing.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void processPayment(ReservationRequestedEvent event) {
        if (paymentRepository.findByReservationId(event.reservationId()).isPresent()) {
            log.warn("Payment already exists for reservationId={}, skipping", event.reservationId());
            return;
        }

        String paymentId = UUID.randomUUID().toString();

        Payment payment = new Payment(
                paymentId, event.reservationId(),
                event.userId(), event.price()
        );

        try {
            simulatePayment(event.price());

            payment.markCompleted();
            paymentRepository.save(payment);

            eventPublisher.publishEvent(new PaymentCompletedEvent(
                    paymentId, event.reservationId(),
                    event.userId(), event.price(), LocalDateTime.now()
            ));
            log.info("Payment completed: paymentId={}, reservationId={}",
                    paymentId, event.reservationId());

        } catch (Exception e) {
            payment.markFailed(e.getMessage());
            paymentRepository.save(payment);

            eventPublisher.publishEvent(new PaymentFailedEvent(
                    event.reservationId(), event.userId(),
                    e.getMessage(), LocalDateTime.now()
            ));
            log.error("Payment failed: reservationId={}, reason={}",
                    event.reservationId(), e.getMessage());
        }
    }

    private void simulatePayment(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid payment amount: " + amount);
        }
    }
}
