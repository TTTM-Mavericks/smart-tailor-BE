package com.smart.tailor.repository;

import com.smart.tailor.entities.PartOfDesign;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartOfDesignRepository extends JpaRepository<PartOfDesign, UUID> {
    Optional<PartOfDesign> findPartOfDesignByDesign_DesignIDAndPartOfDesignName(UUID designID, String partOfDesignName);

    @Transactional
    @Modifying
    @Query(value = "delete from part_of_design p where p.design_id = ?1", nativeQuery = true)
    void deletePartOfDesignByDesignID(UUID designID);
}
