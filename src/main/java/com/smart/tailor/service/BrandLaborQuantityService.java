package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandLaborQuantityListRequest;
import com.smart.tailor.utils.response.BrandLaborQuantityResponse;

import java.util.List;
import java.util.UUID;

public interface BrandLaborQuantityService {
    void createBrandLaborQuantity(UUID brandID, BrandLaborQuantityListRequest brandLaborQuantityListRequest);

    List<BrandLaborQuantityResponse> findBrandLaborQuantityByBrandID(UUID brandID);

    void updateBrandLaborQuantity(UUID brandID, BrandLaborQuantityListRequest brandLaborQuantityListRequest);
}
