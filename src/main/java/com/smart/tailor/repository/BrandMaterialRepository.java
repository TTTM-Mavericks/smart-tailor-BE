package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandMaterialRepository extends JpaRepository<BrandMaterial, BrandMaterialKey> {
}
