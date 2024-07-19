package com.smart.tailor.repository;

import com.smart.tailor.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByPaymentID(UUID paymentID);
    @Query(nativeQuery = true, value = "SELECT DISTINCT p.* FROM payment p JOIN orders o ON p.order_id = o.order_id WHERE o.order_id = ?1")
    List<Payment> findAllByOrderID(UUID orderID);
}
