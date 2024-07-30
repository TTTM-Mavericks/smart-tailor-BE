package com.smart.tailor.repository;

import com.smart.tailor.entities.SampleProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SampleProductDataRepository extends JpaRepository<SampleProductData, UUID> {
    List<SampleProductData> findAllByOrderID(UUID orderID);
}
