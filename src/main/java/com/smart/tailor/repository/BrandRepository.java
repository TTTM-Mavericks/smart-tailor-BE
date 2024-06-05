package com.smart.tailor.repository;

import com.smart.tailor.entities.Brand;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    Optional<Brand> getBrandByBrandID(UUID brandID);

    @Modifying
    @Transactional
    @Query(value = "insert into brand (brand_id, brand_name, brand_status, create_date, last_modified_date) values (?1, ?2, ?3, current_timestamp, null)", nativeQuery = true)
    void createShortBrand(UUID brandID, String brandName, String brandStatus);

    Optional<Brand> findBrandByBrandName(String brandName);
}