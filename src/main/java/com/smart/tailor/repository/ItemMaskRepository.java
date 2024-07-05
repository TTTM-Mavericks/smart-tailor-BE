package com.smart.tailor.repository;

import com.smart.tailor.entities.ItemMask;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemMaskRepository extends JpaRepository<ItemMask, UUID> {
    @Transactional
    @Modifying
    @Query(value = "delete from item_mask im where im.part_of_design_id = ?1", nativeQuery = true)
    void deleteItemMaskByPartOfDesignID(UUID partOfDesignId);
}
