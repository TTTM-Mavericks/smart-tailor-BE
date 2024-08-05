package com.smart.tailor.repository;

import com.smart.tailor.entities.BrandProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BrandPropertiesRepository extends JpaRepository<BrandProperties, String> {
    List<BrandProperties> getAllByBrand_BrandID(String brandID);

    BrandProperties findByBrand_BrandIDAndSystemPropertiesPropertyID(String brandID, String propertyID);
}
