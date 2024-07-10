package com.smart.tailor.repository;

import com.smart.tailor.entities.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DesignRepository extends JpaRepository<Design, UUID> {
    @Query(nativeQuery = true, value = "select de.* from design de inner join ( select distinct d.design_id from design_detail d join orders o on d.order_id = o.order_id where o.order_id = ?1 ) f on de.design_id = f.design_id")
    Design findByOrderID(UUID orderID);
}
