package com.smart.tailor.repository;

import com.smart.tailor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query(nativeQuery = true, value = "SELECT o.* FROM orders o join design_detail d on d.order_id = o.order_id where o.order_type = 'PARENT_ORDER' and d.design_id = ?1")
    Order findParentOrderByDesignID(UUID designID);
}
