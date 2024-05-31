package com.smart.tailor.repository;

import com.smart.tailor.entities.MaskItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaskItemRepository extends JpaRepository<MaskItem, UUID> {
}
