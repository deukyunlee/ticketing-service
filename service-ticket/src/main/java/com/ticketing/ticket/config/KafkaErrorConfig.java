package com.ticketing.ticket.config;

import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaErrorConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorConfig.class);

    @Bean
    public CommonErrorHandler kafkaErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
            new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, ex) -> new TopicPartition(
                    record.topic() + ".DLT",
                    record.partition()
                )
            );

        DefaultErrorHandler errorHandler =
            new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3));

        errorHandler.setRetryListeners((record, ex, deliveryAttempt) ->
            log.warn(
                "Retry attempt {} for topic={}, partition={}, offset={}",
                deliveryAttempt,
                record.topic(),
                record.partition(),
                record.offset()
            )
        );

        return errorHandler;
    }
}
