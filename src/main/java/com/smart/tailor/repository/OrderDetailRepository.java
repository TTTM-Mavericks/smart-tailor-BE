package com.smart.tailor.repository;

import com.smart.tailor.entities.OrderDetail;
import com.smart.tailor.entities.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
}
