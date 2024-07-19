package com.smart.tailor.repository;

import com.smart.tailor.entities.PayOSData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayOSDataRepository extends JpaRepository<PayOSData, Integer> {
}
