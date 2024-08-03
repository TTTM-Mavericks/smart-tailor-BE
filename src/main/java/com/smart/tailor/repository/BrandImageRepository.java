package com.smart.tailor.repository;

import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BrandImageRepository extends JpaRepository<BrandImage, UUID> {
    List<BrandImage> findByBrand(Brand brand);
}
