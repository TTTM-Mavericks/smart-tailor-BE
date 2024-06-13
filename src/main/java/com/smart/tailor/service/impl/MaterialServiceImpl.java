package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Category;
import com.smart.tailor.entities.Material;
import com.smart.tailor.mapper.MaterialMapper;
import com.smart.tailor.repository.MaterialRepository;
import com.smart.tailor.service.CategoryService;
import com.smart.tailor.service.ExcelExportService;
import com.smart.tailor.service.ExcelImportService;
import com.smart.tailor.service.MaterialService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.MaterialResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final Logger logger = LoggerFactory.getLogger(MaterialServiceImpl.class);
    private final CategoryService categoryService;
    private final MaterialMapper materialMapper;
    private final ExcelImportService excelImportService;
    private final ExcelExportService excelExportService;

    @Override
    public Optional<Material> findByMaterialNameAndCategory_CategoryName(String materialName, String categoryName) {
        return materialRepository.findByMaterialNameAndCategory_CategoryName(materialName.toLowerCase(), categoryName.toLowerCase());
    }

    @Override
    @Transactional
    public APIResponse createMaterial(MaterialRequest materialRequest) {
        if(
                !Utilities.isNonNullOrEmpty(materialRequest.getCategoryName()) ||
                !Utilities.isNonNullOrEmpty(materialRequest.getMaterialName())
        ) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.MISSING_ARGUMENT)
                    .build();
        }

        if(!Utilities.isValidDouble(materialRequest.getHsCode().toString()) || materialRequest.getHsCode() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " hsCode : " + materialRequest.getHsCode())
                    .build();
        }
        Optional<Category> categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName().toLowerCase());
        Category category = categoryOptional.isEmpty() ? categoryService.createCategory(materialRequest.getCategoryName()) : categoryOptional.get();
        Optional<Material> materialOptional = findByMaterialNameAndCategory_CategoryName(materialRequest.getMaterialName().toLowerCase(), materialRequest.getCategoryName().toLowerCase());

        if(materialOptional.isPresent()) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.CONFLICT.value())
                    .message(MessageConstant.MATERIAL_IS_EXISTED)
                    .build();
        }
        var material = materialRepository.save(
                Material
                        .builder()
                        .materialName(materialRequest.getMaterialName().toLowerCase())
                        .category(category)
                        .hsCode(materialRequest.getHsCode())
                        .build()
        );
        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_MATERIAL_SUCCESSFULLY)
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public APIResponse createMaterialByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                    .data(null)
                    .build();
        }
        try {
            var excelData = excelImportService.getCategoryMaterialDataFromExcel(file.getInputStream());

            if(excelData == null){
                return APIResponse
                        .builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.WRONG_TYPE_OF_CATEGORY_AND_MATERIAL_EXCEL_FILE)
                        .data(null)
                        .build();
            }

            Set<MaterialRequest> excelNames = new HashSet<>();
            List<MaterialRequest> uniqueExcelData = new ArrayList<>();
            List<MaterialRequest> duplicateExcelData = new ArrayList<>();

            for(MaterialRequest request : excelData){
                if(!excelNames.add(request)){
                    duplicateExcelData.add(request);
                }else{
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.DUPLICATE_CATEGORY_AND_MATERIAL_IN_EXCEL_FILE)
                        .data(duplicateExcelData)
                        .build();
            }

            List<MaterialRequest> validData = new ArrayList<>();
            List<MaterialRequest> invalidData = new ArrayList<>();
            for(MaterialRequest materialRequest : uniqueExcelData){
                var saveExpertTailoringResponse = createMaterial(materialRequest);
                validData.add(materialRequest);
                if(saveExpertTailoringResponse.getStatus() != HttpStatus.OK.value()){
                    invalidData.add(materialRequest);
                }
            }

            if (invalidData.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.ADD_NEW_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY)
                        .data(validData)
                        .build();
            } else {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.ADD_NEW_CATEGORY_AND_MATERIAL_BY_EXCEL_FILE_FAIL)
                        .data(invalidData)
                        .build();
            }
        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                    .data(null)
                    .build();
        }
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

    @Override
    public List<MaterialResponse> exportCategoryMaterialForBrandByExcel(HttpServletResponse response) throws IOException {
        var materialResponses = findAllMaterials();
        excelExportService.exportCategoryMaterialData(materialResponses, response);
        return materialResponses;
    }
}
