package com.smart.tailor.repository;

import com.smart.tailor.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Brand getBrandByBrandID(UUID brandID);
}
