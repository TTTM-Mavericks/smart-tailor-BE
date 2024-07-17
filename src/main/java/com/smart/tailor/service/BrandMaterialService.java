package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BrandMaterialService {
    void createBrandMaterial(BrandMaterialRequest brandMaterialRequest);

    List<BrandMaterialResponse> getAllBrandMaterial();

    List<BrandMaterialResponse> getAllBrandMaterialByBrandID(UUID brandID);

    void createBrandMaterialByImportExcelData(MultipartFile file, UUID brandID);

    void updateBrandMaterial(BrandMaterialRequest brandMaterialRequest);

    Double getMinPriceByMaterialID(UUID materialID);

    Double getMaxPriceByMaterialID(UUID materialID);
}
