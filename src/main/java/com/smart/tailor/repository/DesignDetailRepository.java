package com.smart.tailor.repository;

import com.smart.tailor.entities.DesignDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DesignDetailRepository extends JpaRepository<DesignDetail, UUID> {
    List<DesignDetail> findAllByOrderOrderID(UUID orderID);

    DesignDetail findDesignDetailByDesignDesignIDAndSizeSizeID(UUID designID, UUID sizeID);
}
