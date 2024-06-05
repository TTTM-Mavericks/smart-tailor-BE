package com.smart.tailor.service;

import com.smart.tailor.entities.Material;
import com.smart.tailor.utils.response.MaterialResponse;

import java.util.List;
import java.util.Optional;

public interface MaterialService {
    Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName);

    MaterialResponse createMaterial(String materialName, String categoryName);

    List<MaterialResponse> findAllMaterials();

    MaterialResponse findByMaterialNameAndCategoryName(String materialName, String categoryName);
}
