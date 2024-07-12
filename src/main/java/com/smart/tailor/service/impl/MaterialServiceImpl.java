package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Material;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.MaterialMapper;
import com.smart.tailor.repository.MaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.CategoryResponse;
import com.smart.tailor.utils.response.ErrorData;
import com.smart.tailor.utils.response.MaterialResponse;
import com.smart.tailor.utils.response.MaterialWithPriceResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        var category = categoryService.findByCategoryName(categoryName)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        return materialRepository.findByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCase(materialName, categoryName);
    }

    @Override
    @Transactional
    public void createMaterial(MaterialRequest materialRequest) {
        var category = categoryService.findByCategoryName(materialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        Optional<Material> categoryMaterialOptional = findByMaterialNameAndCategory_CategoryName(materialRequest.getMaterialName(), materialRequest.getCategoryName());
        Optional<Material> materialOptional = findByMaterialName(materialRequest.getMaterialName());

        if (materialOptional.isPresent() || categoryMaterialOptional.isPresent()) {
            throw new ItemAlreadyExistException(MessageConstant.MATERIAL_IS_EXISTED);
        }

        materialRepository.save(
                Material
                        .builder()
                        .materialName(materialRequest.getMaterialName())
                        .category(category)
                        .hsCode(materialRequest.getHsCode())
                        .unit(materialRequest.getUnit())
                        .basePrice(materialRequest.getBasePrice())
                        .status(true)
                        .build()
        );
    }

    @Override
    @Transactional
    public void createMaterialByExcelFile(MultipartFile file) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            var excelData = excelImportService.getCategoryMaterialDataFromExcel(file.getInputStream());

            if (excelData.isEmpty()) {
                throw new BadRequestException("Category and Material Excel File Has Empty Data");
            }

            Set<MaterialRequest> excelNames = new HashSet<>();
            List<MaterialRequest> uniqueExcelData = new ArrayList<>();
            List<Object> duplicateExcelData = new ArrayList<>();

            for (MaterialRequest request : excelData) {
                if (!excelNames.add(request)) {
                    duplicateExcelData.add(request);
                } else {
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_CATEGORY_AND_MATERIAL_IN_EXCEL_FILE, duplicateExcelData);
            }

            List<Object> invalidData = new ArrayList<>();
            for (MaterialRequest materialRequest : uniqueExcelData) {
                try {
                    createMaterial(materialRequest);
                } catch (ItemNotFoundException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.CAN_NOT_FIND_ANY_CATEGORY;
                    logger.error("Error creating Material: Item not found - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(materialRequest, errorMessage));
                } catch (ItemAlreadyExistException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.MATERIAL_IS_EXISTED;
                    logger.error("Error creating Material: Already exists - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(materialRequest, errorMessage));
                } catch (Exception ex) {
                    logger.error("Error creating Material - {}", ex.getMessage());
                    invalidData.add(new ErrorData(materialRequest, ex.getMessage()));
                }
            }

            if (!invalidData.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("Some Data could not be processed correctly", invalidData);
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
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findAllActiveMaterials() {
        return materialRepository
                .findAll()
                .stream()
                .filter(material -> material.getStatus())
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public MaterialResponse findByMaterialNameAndCategoryName(String materialName, String categoryName) {
        var materialOptional = findByMaterialNameAndCategory_CategoryName(materialName, categoryName);
        if (materialOptional.isPresent()) {
            return materialMapper.mapperToMaterialResponseWithoutPrices(materialOptional.get());
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
        if (Utilities.isStringNotNullOrEmpty(materialID.toString())) {
            var material = materialRepository.findByMaterialID(materialID);
            if (material.isPresent()) {
                return materialMapper.mapperToMaterialResponseWithoutPrices(material.get());
            }
        }
        return null;
    }

    @Override
    public void updateMaterial(UUID materialID, MaterialRequest materialRequest) {
        var material = materialRepository.findById(materialID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL));

        var categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        materialRepository.save(
                Material
                        .builder()
                        .materialID(materialID)
                        .materialName(materialRequest.getMaterialName())
                        .category(categoryOptional)
                        .hsCode(materialRequest.getHsCode())
                        .unit(materialRequest.getUnit())
                        .basePrice(materialRequest.getBasePrice())
                        .status(material.getStatus())
                        .build()
        );
    }

    @Override
    public void updateStatusMaterial(UUID materialID) {
        var material = materialRepository.findByMaterialID(materialID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_MATERIAL));

        material.setStatus(!material.getStatus());
        materialRepository.save(material);
    }

    @Override
    public void generateSampleCategoryMaterialByExportExcel(HttpServletResponse response) throws IOException {
        String[] categoryNames = categoryService
                .findAllCatgories()
                .stream()
                .map(CategoryResponse::getCategoryName)
                .toList().toArray(String[]::new);

        excelExportService.exportSampleCategoryMaterial(response, categoryNames);
    }

    @Override
    public Optional<Material> findByMaterialName(String materialName) {
        return materialRepository.findByMaterialName(materialName);
    }

    @Override
    public Optional<Material> findMaterialByID(UUID materialID) {
        return materialRepository.findById(materialID);
    }

    @Override
    public List<MaterialResponse> findListMaterialByCategoryID(UUID categoryID) {
        var category = categoryService.findCategoryOptionalByID(categoryID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        return materialRepository
                .findListMaterialByCategoryID(categoryID)
                .stream()
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialResponse> findListMaterialByCategoryName(String categoryName) {
        var category = categoryService.findByCategoryName(categoryName)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_CATEGORY));

        return materialRepository
                .findListMaterialByCategoryName("%" + categoryName + "%")
                .stream()
                .map(materialMapper::mapperToMaterialResponseWithoutPrices)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaterialWithPriceResponse> findAllMaterialByExpertTailoringIDAndCategoryID(UUID expertTailoringID, UUID categoryID) {
        return materialRepository
                .findAllMaterialByExpertTailoringIDAndCategoryID(expertTailoringID, categoryID)
                .stream()
                .map(material -> {
                    var minPrice = materialRepository.getMinPriceByMaterialID(material.getMaterialID());
                    var maxPrice = materialRepository.getMaxPriceByMaterialID(material.getMaterialID());
                    return materialMapper.mapperToMaterialResponseWithPrices(material, minPrice, maxPrice);
                })
                .toList();
    }
}
