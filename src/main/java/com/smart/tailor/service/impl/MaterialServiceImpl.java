package com.smart.tailor.service.impl;

import com.smart.tailor.entities.Category;
import com.smart.tailor.entities.Material;
import com.smart.tailor.mapper.MaterialMapper;
import com.smart.tailor.repository.MaterialRepository;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.response.MaterialResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);
    private final CategoryService categoryService;
    private final MaterialMapper materialMapper;

    @Override
    public Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName) {
        return materialRepository.findByMaterialNameAndCategory_CategoryName(materialName.toLowerCase(), categoryName.toLowerCase());
    }

    @Override
    public MaterialResponse createMaterial(String materialName, String categoryName) {
        if(Utilities.isNonNullOrEmpty(materialName) && Utilities.isNonNullOrEmpty(categoryName)){
            Optional<Category> categoryOptional = categoryService.findByCategoryName(categoryName.toLowerCase());
            Category category = categoryOptional.isEmpty() ? categoryService.createCategory(categoryName) : categoryOptional.get();
            Optional<Material> materialOptional = findByMaterialNameAndCategory_CategoryName(materialName.toLowerCase(), categoryName.toLowerCase());
            logger.info("Material Optional {}", materialOptional);
            if(materialOptional.isEmpty()){
                return materialMapper.mapperToMaterialResponse(materialRepository.save(
                        Material
                                .builder()
                                .materialName(materialName.toLowerCase())
                                .category(category)
                                .build())
                );
            }
        }
        return null;
    }

    @Override
    public List<MaterialResponse> findAllMaterials() {
        return materialRepository
                .findAll()
                .stream()
                .map(materialMapper::mapperToMaterialResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialResponse findByMaterialNameAndCategoryName(String materialName, String categoryName) {
        var materialOptional = findByMaterialNameAndCategory_CategoryName(materialName.toLowerCase(), categoryName.toLowerCase());
        if(materialOptional.isPresent()){
            return materialMapper.mapperToMaterialResponse(materialOptional.get());
        }
        return null;
    }
}
