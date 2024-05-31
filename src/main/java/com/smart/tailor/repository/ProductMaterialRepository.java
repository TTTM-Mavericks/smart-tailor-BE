package com.smart.tailor.repository;

import com.smart.tailor.entities.ProductMaterial;
import com.smart.tailor.entities.ProductMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, ProductMaterialKey> {
}
