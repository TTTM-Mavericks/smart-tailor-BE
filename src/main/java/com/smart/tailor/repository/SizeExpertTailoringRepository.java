package com.smart.tailor.repository;

import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.entities.SizeExpertTailoringKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SizeExpertTailoringRepository extends JpaRepository<SizeExpertTailoring, SizeExpertTailoringKey> {
    Optional<SizeExpertTailoring> findSizeExpertTailoringByExpertTailoringExpertTailoringIDAndSizeSizeID(UUID expertTailoringID, UUID sizeID);

    boolean existsByExpertTailoringExpertTailoringNameAndSizeSizeNameAndRatio(
            String expertTailoringName,
            String sizeName,
            Double ratio
    );
}
