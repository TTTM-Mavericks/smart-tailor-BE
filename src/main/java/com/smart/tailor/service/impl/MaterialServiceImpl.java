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
import com.smart.tailor.utils.response.ErrorDetail;
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
                .orElseThrow(() -> new ItemNotFoundException("Can Not Find Category with Category Name: " + materialRequest.getCategoryName()));

        Optional<Material> categoryMaterialOptional = findByMaterialNameAndCategory_CategoryName(materialRequest.getMaterialName(), materialRequest.getCategoryName());
        Optional<Material> materialOptional = findByMaterialName(materialRequest.getMaterialName());

        if (materialOptional.isPresent() || categoryMaterialOptional.isPresent()) {
            throw new ItemAlreadyExistException("Material Information with Material Name " + materialRequest.getMaterialName() + " is existed!");
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

    private Boolean materialDuplicate(MaterialRequest materialRequest)
    {
        return materialRepository.existsByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCaseAndHsCodeAndUnitIgnoreCaseAndBasePrice(
                materialRequest.getMaterialName(),
                materialRequest.getCategoryName(),
                materialRequest.getHsCode(),
                materialRequest.getUnit(),
                materialRequest.getBasePrice()
        );
    }


    @Override
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

            List<ErrorDetail> errorFields = new ArrayList<>();
            for (MaterialRequest materialRequest : uniqueExcelData) {
                List<String> errors = new ArrayList<>();

                var category = categoryService.findByCategoryName(materialRequest.getCategoryName());
                if(category.isEmpty()){
                    errors.add("Can Not Find Category with Category Name: " + materialRequest.getCategoryName());
                }

                var material = materialRepository.findByMaterialNameIgnoreCaseAndCategory_CategoryNameIgnoreCase(materialRequest.getMaterialName(), materialRequest.getCategoryName());
                if(materialDuplicate(materialRequest)){
                   errors.add("Material Information with Material Name: " + materialRequest.getMaterialName() + " is existed!");
                }

                if(errors.size() > 0){
                    errorFields.add(new ErrorDetail(materialRequest, errors));
                } else {
                    var materialID = material.isPresent() ? material.get().getMaterialID() : UUID.randomUUID();
                    materialRepository.save(
                            Material
                                    .builder()
                                    .materialID(materialID)
                                    .materialName(materialRequest.getMaterialName())
                                    .category(category.get())
                                    .hsCode(materialRequest.getHsCode())
                                    .unit(materialRequest.getUnit())
                                    .basePrice(materialRequest.getBasePrice())
                                    .status(true)
                                    .build()
                    );
                }
            }

            if (!errorFields.isEmpty()) {
                throw new ExcelFileInvalidDataTypeException("Some Data could not be processed correctly", errorFields);
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
                .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + materialID));

        var categoryOptional = categoryService.findByCategoryName(materialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Category with CategoryName: " + materialRequest.getCategoryName()));

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
                .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialID: " + materialID));

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
        var category = categoryService.findByCategoryName("Can not find Category with CategoryName: " + categoryName)
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
