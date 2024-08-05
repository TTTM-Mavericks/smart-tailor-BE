package com.smart.tailor.repository;

import com.smart.tailor.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(nativeQuery = true, value = "SELECT o.* FROM orders o join design_detail d on d.order_id = o.order_id where o.order_type = 'PARENT_ORDER' and d.design_id = ?1")
    List<Order> findParentOrderByDesignID(String designID);

    @Query(nativeQuery = true, value = "select o.* from orders o join design_detail d ON d.order_id = o.order_id WHERE d.design_detail_id = ?1")
    Order getOrderByDetailID(String designID);

    @Query(nativeQuery = true, value = "select distinct o.* from orders o join design_detail d ON d.order_id = o.order_id WHERE d.brand_id = ?1")
    List<Order> findOrderByBrandID(String brandID);

    @Query(nativeQuery = true, value = "SELECT DISTINCT o2.* FROM orders o2 JOIN orders o1 ON o2.order_id = o1.parent_order_id JOIN design_detail d ON o1.order_id = d.order_id JOIN design de ON d.design_id = de.design_id WHERE de.user_id = ?1")
    List<Order> findParentOrderByUserID(String userID);
}
