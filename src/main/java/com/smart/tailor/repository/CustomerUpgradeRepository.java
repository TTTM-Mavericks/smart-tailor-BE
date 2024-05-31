package com.smart.tailor.repository;

import com.smart.tailor.entities.CustomerUpgrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerUpgradeRepository extends JpaRepository<CustomerUpgrade, UUID> {
}
