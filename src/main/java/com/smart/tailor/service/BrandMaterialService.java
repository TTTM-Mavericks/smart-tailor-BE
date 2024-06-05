package com.smart.tailor.service;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.BrandMaterialResponse;

import java.util.List;

public interface BrandMaterialService {
    APIResponse createBrandMaterial(BrandMaterialRequest brandMaterialRequest);

    List<BrandMaterialResponse> getAllBrandMaterial();

    List<BrandMaterialResponse> getAllBrandMaterialByBrandName(String brandName);
}
