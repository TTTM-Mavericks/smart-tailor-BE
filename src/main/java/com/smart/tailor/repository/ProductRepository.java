package com.smart.tailor.repository;

import com.smart.tailor.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query(value = "select p.* from product p join brand b on p.brand_id = b.brand_id where b.brand_name = ?1", nativeQuery = true)
    List<Product> findAllProductByBrandName(String brandName);

    @Query(value = "select p.* from product p where p.brand_id = ?1", nativeQuery = true)
    List<Product> findAllProductByBrandID(UUID brandID);

    @Query(value = "select p.* from product p where p.design_id = ?1", nativeQuery = true)
    List<Product> findAllProductByDesignID(UUID designID);

    @Query(value = "select p.* from product p join design d on p.design_id = d.design_id where d.user_id = ?1", nativeQuery = true)
    List<Product> findAllProductByUserID(UUID userID);
}
