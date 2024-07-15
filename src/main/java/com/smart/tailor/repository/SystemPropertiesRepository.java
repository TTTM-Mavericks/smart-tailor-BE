package com.smart.tailor.repository;

import com.smart.tailor.entities.SystemProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemPropertiesRepository extends JpaRepository<SystemProperties, UUID> {
    List<SystemProperties> getAllByPropertyType(String propertyType);
}
