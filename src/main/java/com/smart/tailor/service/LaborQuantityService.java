package com.smart.tailor.service;

import com.smart.tailor.entities.LaborQuantity;
import com.smart.tailor.utils.request.LaborQuantityRequest;
import com.smart.tailor.utils.response.LaborQuantityResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LaborQuantityService {
    void createLaborQuantity(LaborQuantityRequest laborQuantityRequest);

    List<LaborQuantityResponse> findAllLaborQuantity();

    void updateLaborQuantity(UUID laborQuantityID, LaborQuantityRequest laborQuantityRequest);

    Optional<LaborQuantity> findByID(UUID laborQuantityID);
}
