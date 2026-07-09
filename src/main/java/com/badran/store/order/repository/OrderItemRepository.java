package com.badran.store.order.repository;

import com.badran.store.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for persisted order item lines.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
