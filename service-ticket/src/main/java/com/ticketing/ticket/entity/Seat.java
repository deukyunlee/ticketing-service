package com.ticketing.ticket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"event_id", "seatNumber"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private boolean reserved;

    private String reservationId;

    public Seat(Long eventId, String seatNumber) {
        this.eventId = eventId;
        this.seatNumber = seatNumber;
        this.reserved = false;
    }

    public void markReserved(String reservationId) {
        this.reserved = true;
        this.reservationId = reservationId;
    }

    public void markAvailable() {
        this.reserved = false;
        this.reservationId = null;
    }

}
