package com.badran.store.repository;

import com.badran.store.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for persisted order item lines.
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
