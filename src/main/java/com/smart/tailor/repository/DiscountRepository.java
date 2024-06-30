package com.smart.tailor.repository;

import com.smart.tailor.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
    @Query(nativeQuery = true, value = "select * from discount " +
            "where start_date_time <= current_timestamp " +
            "and expired_date_time >= current_timestamp " +
            "and discount_status = true")
    List<Discount> getAllValidDiscount();
}
