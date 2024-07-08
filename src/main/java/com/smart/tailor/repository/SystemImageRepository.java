package com.smart.tailor.repository;

import com.smart.tailor.entities.SystemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemImageRepository extends JpaRepository<SystemImage, UUID> {
    List<SystemImage> findAllByImageType(String imageType);

    List<SystemImage> findAllByIsPremium(Boolean isPremium);
}
