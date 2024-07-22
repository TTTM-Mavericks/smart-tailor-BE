package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandLaborQuantityListRequest;
import com.smart.tailor.utils.request.BrandLaborQuantityRequest;
import com.smart.tailor.utils.response.BrandLaborQuantityResponse;
import com.smart.tailor.utils.response.LaborQuantityResponse;

import java.util.List;
import java.util.UUID;

public interface BrandLaborQuantityService {
    void createBrandLaborQuantity(BrandLaborQuantityListRequest brandLaborQuantityListRequest);

    List<BrandLaborQuantityResponse> findBrandLaborQuantityByBrandID(UUID brandID);

    void updateBrandLaborQuantity(UUID brandID, BrandLaborQuantityRequest brandLaborQuantityListRequest);

    BrandLaborQuantityResponse findLaborQuantityByBrandIDAndBrandQuantity(UUID brandID, Integer brandQuantity);
}
