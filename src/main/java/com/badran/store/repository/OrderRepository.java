package com.badran.store.repository;

import com.badran.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for customer orders and order history queries.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds all orders owned by a user.
     */
    List<Order> findByUserId(Long userId);
}
