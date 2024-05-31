package com.smart.tailor.repository;

import com.smart.tailor.entities.DiscountProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DiscountProductRepository extends JpaRepository<DiscountProduct, UUID> {
}
