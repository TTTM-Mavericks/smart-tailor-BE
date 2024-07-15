package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BrandPropertiesRepository extends JpaRepository<BrandProperties, UUID> {
    List<BrandProperties> getAllByBrand_BrandID(UUID brandID);
}
