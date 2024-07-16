package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.entities.Material;
import com.smart.tailor.exception.*;
import com.smart.tailor.mapper.BrandMaterialMapper;
import com.smart.tailor.repository.BrandMaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.request.MaterialRequest;
import com.smart.tailor.utils.response.BrandMaterialResponse;
import com.smart.tailor.utils.response.ErrorDetail;
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
                .orElseThrow(() -> new ItemNotFoundException("Can not find Brand with BrandName: " + brandMaterialRequest.getBrandName()));

        // Check if Category and Material is Existed or not
        var material = materialService.findByMaterialNameAndCategory_CategoryName(brandMaterialRequest.getMaterialName(), brandMaterialRequest.getCategoryName())
                .orElseThrow(() -> new ItemNotFoundException("Can not find Material with MaterialName: " + brandMaterialRequest.getMaterialName()));

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
            throw new BadRequestException("Brand Price must be between " + lowerBound + " and " + upperBound);
        }

        // When All condition pass, store data to BrandMaterial
        BrandMaterialKey brandMaterialKey = BrandMaterialKey
                .builder()
                .brandID(brand.getBrandID())
                .materialID(material.getMaterialID())
                .build();


        brandMaterialRepository.save(
                BrandMaterial
                        .builder()
                        .brandMaterialKey(brandMaterialKey)
                        .material(material)
                        .brand(brand)
                        .brandPrice(brandPrice)
                        .build()
        );
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
                .filter(brandMaterial -> (brandMaterial.getBrand().getBrandName().equalsIgnoreCase(brandName)))
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

            List<ErrorDetail> errorFields = new ArrayList<>();
            for (BrandMaterialRequest brandMaterialRequest : uniqueExcelData) {
                List<String> errors = new ArrayList<>();
                var brand = brandService.findBrandByBrandName(brandMaterialRequest.getBrandName()).orElse(null);
                if(brand == null){
                    errors.add("Can not find Brand with BrandName: " + brandMaterialRequest.getBrandName());
                }
                try{
                     var material = materialService.findByMaterialNameAndCategory_CategoryName(brandMaterialRequest.getMaterialName(),
                             brandMaterialRequest.getCategoryName());

                    if(material.isEmpty()){
                        errors.add("Can not find Material with MaterialName: " + brandMaterialRequest.getMaterialName());
                    }

                    var existedFullMaterial =  materialService.isExistedMaterial(
                            MaterialRequest
                                    .builder()
                                    .materialName(brandMaterialRequest.getMaterialName())
                                    .categoryName(brandMaterialRequest.getCategoryName())
                                    .hsCode(brandMaterialRequest.getHsCode())
                                    .unit(brandMaterialRequest.getUnit())
                                    .basePrice(brandMaterialRequest.getBasePrice())
                                    .build()
                    );

                    if(!existedFullMaterial){{
                        errors.add("Can not find Material Information with Material Information");
                    }}

                    var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(brandMaterialRequest.getCategoryName(),
                            brandMaterialRequest.getMaterialName(), brandMaterialRequest.getBrandName());

                    if (brandMaterialExisted != null && brandMaterialExisted.getBrandPrice().equals(brandMaterialRequest.getBrandPrice())) {
                        errors.add(MessageConstant.BRAND_MATERIAL_IS_EXISTED);
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
                        errors.add("Brand Price must be between " + lowerBound + " and " + upperBound);
                    }

                    if(errors.size() > 0){
                        errorFields.add(new ErrorDetail(brandMaterialRequest, errors));
                    }
                    else{
                        BrandMaterialKey brandMaterialKey = BrandMaterialKey
                                .builder()
                                .brandID(brand.getBrandID())
                                .materialID(material.get().getMaterialID())
                                .build();


                        brandMaterialRepository.save(
                                BrandMaterial
                                        .builder()
                                        .brandMaterialKey(brandMaterialKey)
                                        .material(material.get())
                                        .brand(brand)
                                        .brandPrice(brandPrice)
                                        .build()
                        );
                    }

                } catch (ItemNotFoundException ex) {
                    errors.add(ex.getMessage());
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
        var value = brandMaterialRepository.getMinPriceByMaterialID(materialID);
        if (value == null) {
            return 0.0;
        }
        return value;
    }

    @Override
    public Double getMaxPriceByMaterialID(UUID materialID) {
        var value = brandMaterialRepository.getMaxPriceByMaterialID(materialID);
        if (value == null) {
            return 0.0;
        }
        return value;
    }
}
