package com.smart.tailor.repository;

import com.smart.tailor.entities.OrderStage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderStageRepository extends JpaRepository<OrderStage, UUID> {
}
