package com.ticketing.payment.event;

import com.ticketing.common.KafkaConstants;
import com.ticketing.common.event.PaymentCompletedEvent;
import com.ticketing.common.event.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// TODO: Transactional Outbox Pattern 도입 검토
//  현재 @TransactionalEventListener(AFTER_COMMIT)으로 트랜잭션 롤백 시 Kafka 메시지 전송을 방지하지만,
//  DB 커밋 후 Kafka 전송 실패 시 데이터 불일치가 발생할 수 있음.
//  Outbox 테이블에 이벤트를 저장하고 별도 폴링으로 Kafka에 발행하는 방식으로 개선 필요.
@Component
public class PaymentKafkaPublisher {

    private static final Logger log = LoggerFactory.getLogger(PaymentKafkaPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentKafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        kafkaTemplate.send(KafkaConstants.PAYMENT_COMPLETED_TOPIC, event.reservationId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish payment-completed: reservationId={}", event.reservationId(), ex);
                    } else {
                        log.info("Published payment-completed: reservationId={}", event.reservationId());
                    }
                });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        kafkaTemplate.send(KafkaConstants.PAYMENT_FAILED_TOPIC, event.reservationId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish payment-failed: reservationId={}", event.reservationId(), ex);
                    } else {
                        log.info("Published payment-failed: reservationId={}", event.reservationId());
                    }
                });
    }
}
