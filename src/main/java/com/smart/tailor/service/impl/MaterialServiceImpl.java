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
import com.smart.tailor.utils.response.CategoryResponse;
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

    private APIResponse checkValidMaterialRequestData(MaterialRequest materialRequest){
        if(
                !Utilities.isNonNullOrEmpty(materialRequest.getCategoryName()) ||
                !Utilities.isNonNullOrEmpty(materialRequest.getMaterialName()) ||
                !Utilities.isNonNullOrEmpty(materialRequest.getUnit())
        ) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .build();
        }

        if(!Utilities.isValidDouble(materialRequest.getHsCode().toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " hsCode : " + materialRequest.getHsCode().toString())
                    .build();
        }

        if(materialRequest.getHsCode() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER + " hsCode : " + materialRequest.getHsCode())
                    .build();
        }

        if(!Utilities.isValidDouble(materialRequest.getBasePrice().toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " basePrice : " + materialRequest.getBasePrice().toString())
                    .build();
        }

        if(materialRequest.getBasePrice() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER + " basePrice : " + materialRequest.getBasePrice())
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public APIResponse createMaterial(MaterialRequest materialRequest) {
        var checkValidMaterial = checkValidMaterialRequestData(materialRequest);
        if (checkValidMaterial != null) {
            return checkValidMaterial;
        }

        Optional<Category> categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName().toLowerCase());

        Category category = null;
        if(categoryOptional.isEmpty()){
            var categoryResponse = categoryService.createCategory(materialRequest.getCategoryName());
            category = categoryService.mapperToCategory((CategoryResponse) categoryResponse.getData());
        }
        else category = categoryOptional.get();

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
                        .unit(materialRequest.getUnit())
                        .basePrice(materialRequest.getBasePrice())
                        .status(true)
                        .build()
        );
        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_MATERIAL_SUCCESSFULLY)
                .data(materialMapper.mapperToMaterialResponse(material))
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public APIResponse createMaterialByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                    .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                    .data(null)
                    .build();
        }
        try {
            var apiResponse = excelImportService.getCategoryMaterialDataFromExcel(file.getInputStream());

            if(apiResponse.getStatus() == HttpStatus.BAD_REQUEST.value() ||
                    apiResponse.getStatus() == HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()){
                return apiResponse;
            }

            var excelData = (List<MaterialRequest>) apiResponse.getData();

            if(excelData.isEmpty()){
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.CATEGORY_AND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA)
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
    public List<MaterialResponse> findAllActiveMaterials() {
        return materialRepository
                .findAll()
                .stream()
                .filter(material -> material.getStatus())
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
        var materialResponses = findAllActiveMaterials();
        excelExportService.exportCategoryMaterialForBrand(materialResponses, response);
        return materialResponses;
    }

    @Override
    public MaterialResponse findByMaterialID(UUID materialID) {
        if(Utilities.isStringNotNullOrEmpty(materialID.toString())){
            var material = materialRepository.findByMaterialID(materialID);
            if(material.isPresent()){
                return materialMapper.mapperToMaterialResponse(material.get());
            }
        }
        return null;
    }

    @Override
    public APIResponse updateMaterial(UUID materialID, MaterialRequest materialRequest) {
        if(!Utilities.isStringNotNullOrEmpty(materialID.toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .build();
        }

        var checkValidMaterial = checkValidMaterialRequestData(materialRequest);
        if(checkValidMaterial != null){
            return checkValidMaterial;
        }

        var material = findByMaterialID(materialID);
        if(material == null){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL)
                    .build();
        }

        Optional<Category> categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName().toLowerCase());
        if(categoryOptional.isEmpty()){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY)
                    .build();
        }

        var updateMaterial = materialRepository.save(
                Material
                        .builder()
                        .materialID(materialID)
                        .materialName(materialRequest.getMaterialName().toLowerCase())
                        .category(categoryOptional.get())
                        .hsCode(materialRequest.getHsCode())
                        .unit(materialRequest.getUnit())
                        .basePrice(materialRequest.getBasePrice())
                        .status(material.getStatus())
                        .build()
        );

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_MATERIAL_SUCCESSFULLY)
                .data(materialMapper.mapperToMaterialResponse(updateMaterial))
                .build();
    }

    @Override
    public APIResponse updateStatusMaterial(UUID materialID) {
        if(!Utilities.isStringNotNullOrEmpty(materialID.toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .build();
        }

        var material = materialRepository.findByMaterialID(materialID);
        if(material.isEmpty()){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL)
                    .build();
        }

        material.get().setStatus(material.get().getStatus() ? false : true);

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_MATERIAL_SUCCESSFULLY)
                .data(materialMapper.mapperToMaterialResponse(materialRepository.save(material.get())))
                .build();
    }

    @Override
    public void generateSampleCategoryMaterialByExportExcel(HttpServletResponse response) throws IOException {
        excelExportService.exportSampleCategoryMaterial(response);
    }
}
