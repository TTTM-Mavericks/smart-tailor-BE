package com.smart.tailor.repository;

import com.smart.tailor.entities.ExpertTailoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpertTailoringRepository extends JpaRepository<ExpertTailoring, UUID> {
    Optional<ExpertTailoring> findByExpertTailoringName(String expertTailoringName);
}
