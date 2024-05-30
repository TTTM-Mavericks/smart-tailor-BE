package com.smart.tailor.repository;

import com.smart.tailor.entities.UsingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsingImageRepository extends JpaRepository<UsingImage, UUID> {
    Optional<UsingImage> getUsingImageByTypeAndImageRelationID(String type, UUID imageRelationID);
}
