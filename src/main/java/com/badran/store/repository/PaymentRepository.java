package com.badran.store.repository;

import com.badran.store.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

/**
 * Repository for payment records associated with orders.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Finds the first payment for an order matching any supplied status.
     */
    Optional<Payment> findFirstByOrderOrderIdAndStatusIn(Long orderId, Collection<String> statuses);
}
