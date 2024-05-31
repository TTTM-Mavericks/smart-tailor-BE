package com.smart.tailor.repository;

import com.smart.tailor.entities.PartOfDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartOfDesignRepository extends JpaRepository<PartOfDesign, UUID> {
}
