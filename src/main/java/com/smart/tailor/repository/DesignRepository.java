package com.smart.tailor.repository;

import com.smart.tailor.entities.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DesignRepository extends JpaRepository<Design, UUID> {
}
