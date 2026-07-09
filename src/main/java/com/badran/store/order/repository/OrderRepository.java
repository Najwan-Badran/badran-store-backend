package com.badran.store.order.repository;

import com.badran.store.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for customer orders and order history queries.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds all orders owned by a user.
     */
    List<Order> findByUserId(Long userId);

    /**
     * Finds an order by its human-readable order number.
     */
    Optional<Order> findByOrderNumber(String orderNumber);
}
