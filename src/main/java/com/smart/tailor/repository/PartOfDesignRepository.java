package com.smart.tailor.repository;

import com.smart.tailor.entities.PartOfDesign;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PartOfDesignRepository extends JpaRepository<PartOfDesign, String> {
    Optional<PartOfDesign> findPartOfDesignByDesign_DesignIDAndPartOfDesignName(String designID, String partOfDesignName);

    @Transactional
    @Modifying
    @Query(value = "delete from part_of_design p where p.design_id = ?1", nativeQuery = true)
    void deletePartOfDesignByDesignID(String designID);
}
