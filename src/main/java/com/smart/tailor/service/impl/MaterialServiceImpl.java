package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Category;
import com.smart.tailor.entities.Material;
import com.smart.tailor.exception.ExcelFileDuplicateDataException;
import com.smart.tailor.exception.ExcelFileInvalidFormatException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
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

    @Override
    @Transactional
    public APIResponse createMaterial(MaterialRequest materialRequest) {
        Optional<Category> categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName().toLowerCase());

        Category category = null;
        if(categoryOptional.isEmpty()){
            var categoryResponse = categoryService.createCategory(materialRequest.getCategoryName());
            category = categoryService.mapperToCategory((CategoryResponse) categoryResponse.getData());
        }
        else category = categoryOptional.get();

        Optional<Material> categoryMaterialOptional = findByMaterialNameAndCategory_CategoryName(materialRequest.getMaterialName().toLowerCase(), materialRequest.getCategoryName().toLowerCase());
        Optional<Material> materialOptional = findByMaterialName(materialRequest.getMaterialName().toLowerCase());

        if(materialOptional.isPresent() || categoryMaterialOptional.isPresent()) {
            throw new ItemAlreadyExistException(MessageConstant.MATERIAL_IS_EXISTED);
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
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            var apiResponse = excelImportService.getCategoryMaterialDataFromExcel(file.getInputStream());

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
            List<Object> duplicateExcelData = new ArrayList<>();

            for(MaterialRequest request : excelData){
                if(!excelNames.add(request)){
                    duplicateExcelData.add(request);
                }else{
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_CATEGORY_AND_MATERIAL_IN_EXCEL_FILE, duplicateExcelData);
            }

            List<MaterialRequest> validData = new ArrayList<>();
            List<Object> invalidData = new ArrayList<>();
            for(MaterialRequest materialRequest : uniqueExcelData){
                try{
                    createMaterial(materialRequest);
                    validData.add(materialRequest);
                } catch (ItemAlreadyExistException ex){
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
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_BRAND_MATERIAL_IN_EXCEL_FILE, invalidData);
            }
        } catch (IOException ex) {
            logger.error("Error processing excel file", ex);
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
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
        var material = findByMaterialID(materialID);
        if(material == null){
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
        }

        Optional<Category> categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName().toLowerCase());
        if(categoryOptional.isEmpty()){
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY);
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
        var material = materialRepository.findByMaterialID(materialID);
        if(material.isEmpty()){
            throw new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL);
        }

        material.get().setStatus(!material.get().getStatus());

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

    @Override
    public Optional<Material> findByMaterialName(String materialName) {
        return materialRepository.findByMaterialName(materialName);
    }
}
