package com.ticketing.payment.repository;

import com.ticketing.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByReservationId(String reservationId);
}
