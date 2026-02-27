package com.ticketing.ticket.repository;

import com.ticketing.ticket.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByEventId(Long eventId);

    List<Seat> findByEventIdAndReserved(Long eventId, boolean reserved);

    Optional<Seat> findByEventIdAndSeatNumber(Long eventId, String seatNumber);
}
