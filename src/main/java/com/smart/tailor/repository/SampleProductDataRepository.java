package com.smart.tailor.repository;

import com.smart.tailor.entities.SampleProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SampleProductDataRepository extends JpaRepository<SampleProductData, UUID> {
    @Query(nativeQuery = true, value = "select d.* from sample_product_data d join order_stage s on d.stage_id = s.stage_id join orders o on s.order_id = o.order_id where o.order_id = ?1 or o.parent_order_id = ?1")
    List<SampleProductData> findSampleProductDataByOrderID(UUID orderID);
}
