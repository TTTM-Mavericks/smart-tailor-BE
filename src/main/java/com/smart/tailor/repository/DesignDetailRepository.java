package com.smart.tailor.repository;

import com.smart.tailor.entities.DesignDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignDetailRepository extends JpaRepository<DesignDetail, UUID> {
    @Query(nativeQuery = true, value = "SELECT DISTINCT d.* FROM design_detail d JOIN orders o ON d.order_id = o.order_id WHERE o.parent_order_id = ?1 Or o.order_id = ?1")
    List<DesignDetail> findAllByOrderID(UUID orderID);

    @Query(nativeQuery = true, value = "SELECT DISTINCT d.* FROM design_detail d JOIN orders o ON d.order_id = o.order_id WHERE o.order_id = ?1")
    List<DesignDetail> findAllBySubOrderID(UUID orderID);

    DesignDetail findDesignDetailByDesignDesignIDAndSizeSizeID(UUID designID, UUID sizeID);

    @Query(nativeQuery = true, value = "SELECT d.* FROM design_detail d JOIN orders o ON d.order_id = o.order_id WHERE o.order_type = 'SUB_ORDER' AND o.parent_order_id = ?1 AND d.brand_id = ?2")
    DesignDetail getDetailOfOrderBaseOnBrandID(UUID orderID, UUID brandID);

    Optional<DesignDetail> getDesignDetailByDesignDetailID(UUID designDetailID);

    @Query(nativeQuery = true, value = "SELECT d.* FROM design_detail d JOIN orders o ON d.order_id = o.order_id WHERE o.order_id = ?1")
    List<DesignDetail> getDesignDetailBySubOrderID(UUID subOrderID);
}
