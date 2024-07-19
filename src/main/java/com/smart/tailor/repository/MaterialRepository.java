package com.smart.tailor.repository;

import com.smart.tailor.entities.Material;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    Optional<Material> findByMaterialID(UUID materialID);

    Optional<Material> findByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCase(String materialName, String categoryName);

    Optional<Material> findByMaterialName(String materialName);

    @Query(value = "select * from material where category_id = ?1", nativeQuery = true)
    List<Material> findListMaterialByCategoryID(UUID materialID);

    @Query(value = "select m.* from material m join category c on m.category_id = c.category_id where c.category_name like ?1", nativeQuery = true)
    List<Material> findListMaterialByCategoryName(String categoryName);

    @Query(value = "select m.* from material m join category c on m.category_id = c.category_id " +
            "join expert_tailoring_material etm on m.material_id = etm.material_id " +
            "join expert_tailoring et on et.expert_tailoring_id = etm.expert_tailoring_id " +
            "where et.expert_tailoring_id = ?1 && c.category_id = ?2 && etm.status = true", nativeQuery = true)
    List<Material>findAllMaterialByExpertTailoringIDAndCategoryID(UUID expertTailoringID, UUID categoryID);

    @Query(value = "SELECT MIN(bm.brand_price) FROM brand_material bm join material m on m.material_id = bm.material_id where m.material_id = ?1", nativeQuery = true)
    Double getMinPriceByMaterialID(UUID materialID);

    @Query(value = "SELECT MAX(bm.brand_price) FROM brand_material bm join material m on m.material_id = bm.material_id where m.material_id = ?1", nativeQuery = true)
    Double getMaxPriceByMaterialID(UUID materialID);

    boolean existsByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCaseAndHsCodeAndUnitIgnoreCaseAndBasePrice(
            String materialName,
            String categoryName,
            Long hsCode,
            String unit,
            Integer basePrice
    );

    @Query(value = "select m.* from material m join category c on m.category_id = c.category_id " +
            "join expert_tailoring_material etm on m.material_id = etm.material_id " +
            "join expert_tailoring et on et.expert_tailoring_id = etm.expert_tailoring_id " +
            "where et.expert_tailoring_id = ?1 && c.category_name = ?2 && etm.status = true", nativeQuery = true)
    List<Material>findMaterialsByExpertTailoringIDAndCategoryName(UUID expertTailoringID, String categoryName);

}
