package com.smart.tailor.repository;

import com.smart.tailor.entities.SizeExpertTailoring;
import com.smart.tailor.entities.SizeExpertTailoringKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SizeExpertTailoringRepository extends JpaRepository<SizeExpertTailoring, SizeExpertTailoringKey> {
    @Query(value = "select se.* from size s join size_expert_tailoring se on s.size_id = se.size_id\n" +
            "\t\t\t\t\t join expert_tailoring e on e.expert_tailoring_id = se.expert_tailoring_id\n" +
            "where s.size_id = ?1 && e.expert_tailoring_id = ?2", nativeQuery = true)
    SizeExpertTailoring findSizeExpertTailoringBySizeIDAndExpertTailoringID(UUID sizeID, UUID expertTailoringID);
}
