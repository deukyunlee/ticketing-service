package com.ticketing.payment.service;

import com.ticketing.common.event.PaymentCompletedEvent;
import com.ticketing.common.event.PaymentFailedEvent;
import com.ticketing.common.event.ReservationRequestedEvent;
import com.ticketing.payment.entity.Payment;
import com.ticketing.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void processPayment_success_shouldPublishCompletedEvent() {
        ReservationRequestedEvent event = new ReservationRequestedEvent(
                "res-1", "user-1", "1", "A1", 50000, LocalDateTime.now()
        );

        paymentService.processPayment(event);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(Payment.PaymentStatus.COMPLETED);

        ArgumentCaptor<PaymentCompletedEvent> eventCaptor = ArgumentCaptor.forClass(PaymentCompletedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().reservationId()).isEqualTo("res-1");
        assertThat(eventCaptor.getValue().userId()).isEqualTo("user-1");
        assertThat(eventCaptor.getValue().amount()).isEqualTo(50000);
    }

    @Test
    void processPayment_invalidAmount_shouldPublishFailedEvent() {
        ReservationRequestedEvent event = new ReservationRequestedEvent(
                "res-1", "user-1", "1", "A1", 0, LocalDateTime.now()
        );

        paymentService.processPayment(event);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().getStatus()).isEqualTo(Payment.PaymentStatus.FAILED);

        ArgumentCaptor<PaymentFailedEvent> eventCaptor = ArgumentCaptor.forClass(PaymentFailedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().reservationId()).isEqualTo("res-1");
        assertThat(eventCaptor.getValue().reason()).isEqualTo("Invalid payment amount: 0");
    }
}
