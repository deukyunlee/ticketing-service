package com.ticketing.common;

public final class KafkaConstants {

    public static final String RESERVATION_REQUESTED_TOPIC = "reservation-requested";
    public static final String PAYMENT_COMPLETED_TOPIC = "payment-completed";
    public static final String PAYMENT_FAILED_TOPIC = "payment-failed";
    public static final String RESERVATION_CONFIRMED_TOPIC = "reservation-confirmed";
    public static final String RESERVATION_CANCELLED_TOPIC = "reservation-cancelled";

    private KafkaConstants() {
    }
}
