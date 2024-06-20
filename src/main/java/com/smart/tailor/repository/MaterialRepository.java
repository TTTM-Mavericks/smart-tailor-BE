package com.smart.tailor.repository;

import com.smart.tailor.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    Optional<Material> findByMaterialID(UUID materialID);

    Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName);
}
