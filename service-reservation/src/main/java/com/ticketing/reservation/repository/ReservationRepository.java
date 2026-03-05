package com.ticketing.reservation.repository;

import com.ticketing.reservation.entity.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    
    List<Reservation> findByUserId(String userId);
}
