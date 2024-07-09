package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.BrandMaterialMapper;
import com.smart.tailor.repository.BrandMaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import com.smart.tailor.utils.response.ErrorData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.smart.tailor.constant.FormatConstant.PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandMaterialServiceImpl implements BrandMaterialService {
    private final MaterialService materialService;
    private final BrandService brandService;
    private final BrandMaterialRepository brandMaterialRepository;
    private final BrandMaterialMapper brandMaterialMapper;
    private final ExcelImportService excelImportService;
    private final ExcelExportService excelExportService;
    private final Logger logger = LoggerFactory.getLogger(BrandMaterialServiceImpl.class);

    @Override
    @Transactional
    public void createBrandMaterial(BrandMaterialRequest brandMaterialRequest) {
        // Check If Brand Name is Existed
        var brand = brandService.findBrandByBrandName(brandMaterialRequest.getBrandName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_BRAND));

        // Check if Category and Material is Existed or not
        var materialResponse = materialService.findByMaterialNameAndCategoryName(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());
        if (materialResponse == null) {
            throw new ItemNotFoundException(MessageConstant.CATEGORY_AND_MATERIAL_IS_NOT_EXISTED);
        }

        // Check Whether BrandMaterial is Existed or not
        // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
        var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), brandMaterialRequest.getBrandName());
        if (brandMaterialExisted != null) {
            throw new ItemAlreadyExistException(MessageConstant.BRAND_MATERIAL_IS_EXISTED);
        }

        double basePrice = brandMaterialRequest.getBasePrice();
        double brandPrice = brandMaterialRequest.getBrandPrice();
        double percentageFluctuation = PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE;

        double lowerBound = basePrice * (1 - percentageFluctuation);
        double upperBound = basePrice * (1 + percentageFluctuation);

        brandPrice = Utilities.roundToTwoDecimalPlaces(brandPrice);
        lowerBound = Utilities.roundToTwoDecimalPlaces(lowerBound);
        upperBound = Utilities.roundToTwoDecimalPlaces(upperBound);

        if (brandPrice < lowerBound || brandPrice > upperBound) {
            throw new BadRequestException(MessageConstant.BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION);
        }

        // When All condition pass, store data to BrandMaterial
        brandMaterialRepository.createBrandMaterial(brand.getBrandID(), materialResponse.getMaterialID(), brandMaterialRequest.getBrandPrice());
    }

    @Override
    public List<BrandMaterialResponse> getAllBrandMaterial() {
        return brandMaterialRepository
                .findAll()
                .stream()
                .map(brandMaterialMapper::mapperToBrandMaterialResponse)
                .toList();
    }

    @Override
    public List<BrandMaterialResponse> getAllBrandMaterialByBrandName(String brandName) {
        Optional<Brand> brand = brandService.findBrandByBrandName(brandName);
        if (brand.isEmpty()) return null;
        return brandMaterialRepository
                .findAll()
                .stream()
                .filter(brandMaterial -> (brandMaterial.getBrandMaterialKey().getBrand().getBrandName().equalsIgnoreCase(brandName)))
                .map(brandMaterialMapper::mapperToBrandMaterialResponse)
                .toList();
    }

    @Override
    public void createBrandMaterialByImportExcelData(MultipartFile file, String brandName) {
        if (!excelImportService.isValidExcelFile(file)) {
            throw new ExcelFileInvalidFormatException(MessageConstant.INVALID_EXCEL_FILE_FORMAT);
        }
        try {
            var excelData = excelImportService.getBrandMaterialDataFromExcel(file.getInputStream(), brandName);

            if (excelData.isEmpty()) {
                throw new BadRequestException("Brand Material Excel File Has Empty Data");
            }

            Set<BrandMaterialRequest> excelNames = new HashSet<>();
            List<BrandMaterialRequest> uniqueExcelData = new ArrayList<>();
            List<Object> duplicateExcelData = new ArrayList<>();

            for (BrandMaterialRequest request : excelData) {
                if (!excelNames.add(request)) {
                    duplicateExcelData.add(request);
                } else {
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                throw new ExcelFileDuplicateDataException(MessageConstant.DUPLICATE_BRAND_MATERIAL_IN_EXCEL_FILE, duplicateExcelData);
            }

            List<Object> invalidData = new ArrayList<>();

            for (BrandMaterialRequest brandMaterialRequest : uniqueExcelData) {
                try {
                    createBrandMaterial(brandMaterialRequest);
                } catch (ItemNotFoundException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.CAN_NOT_FIND_BRAND;
                    logger.error("Error creating BrandMaterial: Item not found - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(brandMaterialRequest, errorMessage));
                } catch (ItemAlreadyExistException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.BRAND_MATERIAL_IS_EXISTED;
                    logger.error("Error creating BrandMaterial: Already exists - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(brandMaterialRequest, errorMessage));
                } catch (BadRequestException ex) {
                    String errorMessage = ex.getMessage() != null ? ex.getMessage() : MessageConstant.BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION;
                    logger.error("Error creating BrandMaterial: Bad request - {}", errorMessage, ex);
                    invalidData.add(new ErrorData(brandMaterialRequest, errorMessage));
                } catch (Exception ex) {
                    logger.error("Error creating BrandMaterial - {}", ex.getMessage());
                    invalidData.add(new ErrorData(brandMaterialRequest, ex.getMessage()));
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
    public void updateBrandMaterial(BrandMaterialRequest brandMaterialRequest) {
        // Check If Brand Name is Existed
        var brand = brandService.findBrandByBrandName(brandMaterialRequest.getBrandName())
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_BRAND));

        // Check if Category and Material is Existed or not
        var materialResponse = materialService.findByMaterialNameAndCategoryName(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());
        if (materialResponse == null) {
            throw new ItemNotFoundException(MessageConstant.CATEGORY_AND_MATERIAL_IS_NOT_EXISTED);
        }

        // Check Whether BrandMaterial is Existed or not
        // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
        var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), brandMaterialRequest.getBrandName());
        if (brandMaterialExisted == null) {
            throw new ItemAlreadyExistException(MessageConstant.BRAND_MATERIAL_IS_EXISTED);
        }

        double basePrice = brandMaterialExisted.getBrandPrice();
        double brandPrice = brandMaterialRequest.getBrandPrice();
        double percentageFluctuation = PERCENTAGE_FLUCTUATION_WITHIN_LIMIT_RANGE;

        double lowerBound = basePrice * (1 - percentageFluctuation);
        double upperBound = basePrice * (1 + percentageFluctuation);

        brandPrice = Utilities.roundToTwoDecimalPlaces(brandPrice);
        lowerBound = Utilities.roundToTwoDecimalPlaces(lowerBound);
        upperBound = Utilities.roundToTwoDecimalPlaces(upperBound);

        if (brandPrice < lowerBound || brandPrice > upperBound) {
            throw new BadRequestException(MessageConstant.BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION);
        }

        brandMaterialRepository.updateBrandMaterial(brandPrice, brandMaterialExisted.getBrandMaterialKey().getBrandID(), brandMaterialExisted.getBrandMaterialKey().getMaterialID());
    }

    @Override
    public Double getMinPriceByMaterialID(UUID materialID) {
        return brandMaterialRepository.getMinPriceByMaterialID(materialID);
    }

    @Override
    public Double getMaxPriceByMaterialID(UUID materialID) {
        return brandMaterialRepository.getMaxPriceByMaterialID(materialID);
    }
}
