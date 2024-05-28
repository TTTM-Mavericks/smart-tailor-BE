package com.smart.tailor.repository;

import com.smart.tailor.entities.UsingImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UsingImageRepository extends JpaRepository<UsingImage, UUID> {
}
