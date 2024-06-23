package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.mapper.BrandMaterialMapper;
import com.smart.tailor.repository.BrandMaterialRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandMaterialRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.BrandMaterialResponse;
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
    public APIResponse createBrandMaterial(BrandMaterialRequest brandMaterialRequest) {
        if(
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getBrandName()) ||
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getMaterialName()) ||
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getCategoryName()) ||
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getUnit())
        ){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .data(null)
                    .build();
        }
        if(!Utilities.isValidDouble(brandMaterialRequest.getHsCode().toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " hsCode : " + brandMaterialRequest.getHsCode().toString())
                    .build();
        }

        if(brandMaterialRequest.getHsCode() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER + " hsCode : " + brandMaterialRequest.getHsCode())
                    .build();
        }

        if(!Utilities.isValidDouble(brandMaterialRequest.getBasePrice().toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " basePrice : " + brandMaterialRequest.getBasePrice().toString())
                    .build();
        }

        if(brandMaterialRequest.getBasePrice() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER + " basePrice : " + brandMaterialRequest.getBasePrice())
                    .build();
        }

        if(!Utilities.isValidDouble(brandMaterialRequest.getBrandPrice().toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " brandPrice : " + brandMaterialRequest.getBrandPrice().toString())
                    .build();
        }

        if(brandMaterialRequest.getBrandPrice() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER + " brandPrice : " + brandMaterialRequest.getBrandPrice())
                    .build();
        }

        // Check If Brand Name is Existed
        Optional<Brand> brand = brandService.findBrandByBrandName(brandMaterialRequest.getBrandName());
        if(brand.isEmpty()) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_BRAND)
                    .build();
        }

        // Check if Category and Material is Existed or not
        var materialResponse = materialService.findByMaterialNameAndCategoryName(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());
        if(materialResponse == null){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CATEGORY_AND_MATERIAL_IS_NOT_EXISTED)
                    .build();
        }

        // Check Whether BrandMaterial is Existed or not
        // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
        var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), brandMaterialRequest.getBrandName());
        if(brandMaterialExisted != null) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.BRAND_MATERIAL_IS_EXISTED)
                    .build();
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
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION)
                    .build();
        }

        // When All condition pass, store data to BrandMaterial
        brandMaterialRepository.createBrandMaterial(brand.get().getBrandID(), materialResponse.getMaterialID(), brandMaterialRequest.getBrandPrice());

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.ADD_NEW_BRAND_MATERIAL_SUCCESSFULLY)
                .data(null)
                .build();
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
        if(brand.isEmpty()) return null;
        return brandMaterialRepository
                .findAll()
                .stream()
                .filter(brandMaterial -> (brandMaterial.getBrandMaterialKey().getBrand().getBrandName().equalsIgnoreCase(brandName)))
                .map(brandMaterialMapper::mapperToBrandMaterialResponse)
                .toList();
    }

    @Override
    public APIResponse createBrandMaterialByImportExcelData(MultipartFile file, String brandName) {
        if (!excelImportService.isValidExcelFile(file)) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_EXCEL_FILE_FORMAT)
                    .data(null)
                    .build();
        }
        try {
            var apiResponse = excelImportService.getBrandMaterialDataFromExcel(file.getInputStream(), brandName);
            System.out.println(apiResponse.getData());

            if(apiResponse.getStatus() == HttpStatus.BAD_REQUEST.value() ||
                    apiResponse.getStatus() == HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()){
                return apiResponse;
            }

            var excelData = (List<BrandMaterialRequest>) apiResponse.getData();

            if(excelData.isEmpty()){
                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.BRAND_MATERIAL_EXCEL_FILE_HAS_EMPTY_DATA)
                        .data(null)
                        .build();
            }

            Set<BrandMaterialRequest> excelNames = new HashSet<>();
            List<BrandMaterialRequest> uniqueExcelData = new ArrayList<>();
            List<BrandMaterialRequest> duplicateExcelData = new ArrayList<>();

            for(BrandMaterialRequest request : excelData){
                if(!excelNames.add(request)){
                    duplicateExcelData.add(request);
                }else{
                    uniqueExcelData.add(request);
                }
            }

            if (!duplicateExcelData.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.DUPLICATE_BRAND_MATERIAL_IN_EXCEL_FILE)
                        .data(duplicateExcelData)
                        .build();
            }

            List<BrandMaterialRequest> validData = new ArrayList<>();
            List<BrandMaterialRequest> invalidData = new ArrayList<>();
            for(BrandMaterialRequest brandMaterialRequest : uniqueExcelData){
                var saveExpertTailoringResponse = createBrandMaterial(brandMaterialRequest);
                validData.add(brandMaterialRequest);
                if(saveExpertTailoringResponse.getStatus() != HttpStatus.OK.value()){
                    invalidData.add(brandMaterialRequest);
                }
            }

            if (invalidData.isEmpty()) {
                return APIResponse.builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.ADD_NEW_BRAND_MATERIAL_BY_EXCEL_FILE_SUCCESSFULLY)
                        .data(validData)
                        .build();
            } else {
                return APIResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(MessageConstant.BRAND_MATERIAL_IS_EXISTED)
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
    public APIResponse updateBrandMaterial(BrandMaterialRequest brandMaterialRequest) {
        if(
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getBrandName()) ||
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getMaterialName()) ||
                !Utilities.isNonNullOrEmpty(brandMaterialRequest.getCategoryName())
        ){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.DATA_IS_EMPTY)
                    .data(null)
                    .build();
        }

        if(!Utilities.isValidDouble(brandMaterialRequest.getBrandPrice().toString())){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE + " brandPrice : " + brandMaterialRequest.getBrandPrice().toString())
                    .build();
        }

        if(brandMaterialRequest.getBrandPrice() < 0){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_NEGATIVE_NUMBER_NEED_POSITIVE_NUMBER + " brandPrice : " + brandMaterialRequest.getBrandPrice())
                    .build();
        }

        // Check If Brand Name is Existed
        Optional<Brand> brand = brandService.findBrandByBrandName(brandMaterialRequest.getBrandName());
        if(brand.isEmpty()) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CAN_NOT_FIND_BRAND)
                    .build();
        }

        // Check if Category and Material is Existed or not
        var materialResponse = materialService.findByMaterialNameAndCategoryName(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());
        if(materialResponse == null){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.CATEGORY_AND_MATERIAL_IS_NOT_EXISTED)
                    .build();
        }

        // Check Whether BrandMaterial is Existed or not
        // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
        var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), brandMaterialRequest.getBrandName());
        if(brandMaterialExisted == null) {
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.BRAND_MATERIAL_IS_EXISTED)
                    .build();
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
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.BRAND_PRICE_MUST_BE_BETWEEN_BASE_PRICE_MULTIPLE_WITH_PERCENTAGE_FLUCTUATION)
                    .build();
        }

        brandMaterialRepository.updateBrandMaterial(brandPrice, brandMaterialExisted.getBrandMaterialKey().getBrandID(), brandMaterialExisted.getBrandMaterialKey().getMaterialID());

        return APIResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(MessageConstant.UPDATE_BRAND_MATERIAL_SUCCESSFULLY)
                .build();
    }
}
