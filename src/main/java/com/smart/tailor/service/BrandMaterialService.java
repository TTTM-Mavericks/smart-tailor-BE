package com.smart.tailor.service;

import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;


public interface BrandMaterialService {
    void createBrandMaterial(BrandMaterialRequest brandMaterialRequest);

    List<BrandMaterialResponse> getAllBrandMaterial();

    List<BrandMaterialResponse> getAllBrandMaterialByBrandID(String brandID);

    void createBrandMaterialByImportExcelData(MultipartFile file, String brandID);

    void updateBrandMaterial(BrandMaterialRequest brandMaterialRequest);

    Integer getMinPriceByMaterialID(String materialID);

    Integer getMaxPriceByMaterialID(String materialID);

    Optional<BrandMaterial> getPriceByID(BrandMaterialKey key);

    Integer getBrandPriceByBrandIDAndMaterialID(String brandID, String materialID);
}
