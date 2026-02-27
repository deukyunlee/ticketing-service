package com.ticketing.payment.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String reservationId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String failReason;
    private LocalDateTime createdAt;

    protected Payment() {
    }

    public Payment(String id, String reservationId, String userId, long amount) {
        this.id = id;
        this.reservationId = reservationId;
        this.userId = userId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public String getReservationId() { return reservationId; }
    public String getUserId() { return userId; }
    public long getAmount() { return amount; }
    public PaymentStatus getStatus() { return status; }
    public String getFailReason() { return failReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void markCompleted() {
        this.status = PaymentStatus.COMPLETED;
    }

    public void markFailed(String reason) {
        this.status = PaymentStatus.FAILED;
        this.failReason = reason;
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, FAILED
    }
}
