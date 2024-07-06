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

    Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName);

    Optional<Material> findByMaterialName(String materialName);

    @Query(value = "select * from material where category_id = ?1", nativeQuery = true)
    List<Material> findListMaterialByCategoryID(UUID materialID);

    @Query(value = "select m.* from material m join category c on m.category_id = c.category_id where c.category_name like ?1", nativeQuery = true)
    List<Material> findListMaterialByCategoryName(String categoryName);
}
