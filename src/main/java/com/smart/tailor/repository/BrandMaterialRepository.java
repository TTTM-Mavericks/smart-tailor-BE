package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandMaterialRepository extends JpaRepository<BrandMaterial, BrandMaterialKey> {
    @Modifying
    @Transactional
    @Query(value = "insert into brand_material (brand_id, material_id, price, unit, create_date, last_modified_date) values (?1, ?2, ?3, ?4, current_timestamp, null)", nativeQuery = true)
    void createBrandMaterial(UUID brandID, UUID materialID, Double price, String unit);

    @Query(
            value = "select bm.* from brand_material bm join brand b on bm.brand_id = b.brand_id " +
                    "join material m on m.material_id = bm.material_id " +
                    "join category c on c.category_id = m.category_id " +
                    "where c.category_name = ?1 and m.material_name = ?2 and b.brand_name = ?3", nativeQuery = true
    )
    BrandMaterial findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(String categoryName, String materialName, String brandName);
}
