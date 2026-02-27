package com.ticketing.reservation.entity;

import com.ticketing.common.ReservationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String eventId;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected Reservation() {
    }

    public Reservation(String id, String userId, String eventId,
                       String seatNumber, long price) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.status = ReservationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getEventId() { return eventId; }
    public String getSeatNumber() { return seatNumber; }
    public long getPrice() { return price; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
