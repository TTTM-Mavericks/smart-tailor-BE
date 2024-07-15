package com.smart.tailor.repository;

import com.smart.tailor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query(nativeQuery = true, value = "SELECT o.* FROM orders o join design_detail d on d.order_id = o.order_id where o.order_type = 'PARENT_ORDER' and d.design_id = ?1")
    List<Order> findParentOrderByDesignID(UUID designID);

    @Query(nativeQuery = true, value = "select o.* from orders o join design_detail d ON d.order_id = o.order_id WHERE d.design_detail_id = ?1")
    Order getOrderByDetailID(UUID designID);
}
