package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandLaborQuantity;
import com.smart.tailor.entities.BrandLaborQuantityKey;
import com.smart.tailor.entities.LaborQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandLaborQuantityRepository extends JpaRepository<BrandLaborQuantity, BrandLaborQuantityKey> {
}
