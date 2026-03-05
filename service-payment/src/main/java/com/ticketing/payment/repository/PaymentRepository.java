package com.ticketing.payment.repository;

import com.ticketing.payment.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Optional<Payment> findByReservationId(String reservationId);
}
