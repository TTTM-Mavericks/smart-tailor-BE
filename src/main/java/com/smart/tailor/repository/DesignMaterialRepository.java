package com.smart.tailor.repository;

import com.smart.tailor.entities.DesignMaterial;
import com.smart.tailor.entities.DesignMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DesignMaterialRepository extends JpaRepository<DesignMaterial, DesignMaterialKey> {
}
