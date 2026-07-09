package com.badran.store.order.repository;

import com.badran.store.order.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for payment records associated with orders.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    /**
     * Finds all payment records for an order.
     */
    List<Payment> findByOrderOrderId(Long orderId);
}
