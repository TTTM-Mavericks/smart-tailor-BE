package com.smart.tailor.service;

import com.smart.tailor.entities.Material;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MaterialService {
    Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName);

    APIResponse createMaterial(MaterialRequest materialRequest);

    List<MaterialResponse> findAllMaterials();

    MaterialResponse findByMaterialNameAndCategoryName(String materialName, String categoryName);

    APIResponse createMaterialByExcelFile(MultipartFile file);

    List<MaterialResponse> exportCategoryMaterialForBrandByExcel(HttpServletResponse response) throws IOException;
}
