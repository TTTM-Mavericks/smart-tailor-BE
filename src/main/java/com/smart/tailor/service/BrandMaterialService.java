package com.smart.tailor.service;

import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BrandMaterialService {
    APIResponse createBrandMaterial(BrandMaterialRequest brandMaterialRequest);

    List<BrandMaterialResponse> getAllBrandMaterial();

    List<BrandMaterialResponse> getAllBrandMaterialByBrandName(String brandName);

    APIResponse createBrandMaterialByImportExcelData(MultipartFile file, String brandName);

//    List<BrandMaterialResponse> getAllBrandMaterialByExportExcelData(HttpServletResponse response) throws IOException;
}
