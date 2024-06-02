package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BrandRegistrationRepository extends JpaRepository<BrandRegistration, UUID> {
}
