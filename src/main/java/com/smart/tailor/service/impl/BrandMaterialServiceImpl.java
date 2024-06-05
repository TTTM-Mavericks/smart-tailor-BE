package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.BrandMaterial;
import com.smart.tailor.entities.BrandMaterialKey;
import com.smart.tailor.mapper.BrandMaterialMapper;
import com.smart.tailor.repository.BrandMaterialRepository;
import com.smart.tailor.service.BrandMaterialService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.MaterialService;
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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandMaterialServiceImpl implements BrandMaterialService {
    private final MaterialService materialService;
    private final BrandService brandService;
    private final BrandMaterialRepository brandMaterialRepository;
    private final BrandMaterialMapper brandMaterialMapper;
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
                    .message(MessageConstant.MISSING_ARGUMENT)
                    .data(null)
                    .build();
        }
        if(
                !Utilities.isValidDouble(brandMaterialRequest.getPrice().toString())
        ){
            return APIResponse
                    .builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message(MessageConstant.INVALID_DATA_TYPE)
                    .data(null)
                    .build();
        }
        // Check If Brand Name is Existed
        Optional<Brand> brand = brandService.findBrandByBrandName(brandMaterialRequest.getBrandName());
        if(brand.isPresent()){
            // Check Whether BrandMaterial is Existed or Not
            // If Existed ==> Fail to Store Brand Material because Each Brand only enter one MaterialName belong to one CategoryName
            var brandMaterialExisted = brandMaterialRepository.findBrandMaterialByCategoryNameAndMaterialNameAndBrandName(brandMaterialRequest.getCategoryName(), brandMaterialRequest.getMaterialName(), brandMaterialRequest.getBrandName());
            if(brandMaterialExisted == null){
                // Check Whether MaterialName and CategoryName is Existed or Not
                // If not Existed ==> Create Material
                var materialResponse = materialService.findByMaterialNameAndCategoryName(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());
                var material = (materialResponse != null) ? materialResponse :  materialService.createMaterial(brandMaterialRequest.getMaterialName().toLowerCase(), brandMaterialRequest.getCategoryName().toLowerCase());

                // When All Condition is pass, store data to BrandMaterial
                brandMaterialRepository.createBrandMaterial(brand.get().getBrandID(), material.getMaterialID(), Double.parseDouble(brandMaterialRequest.getPrice()), brandMaterialRequest.getUnit().toLowerCase());

                return APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.ADD_NEW_BRAND_MATERIAL_SUCCESSFULLY)
                        .data(null)
                        .build();
            }

        }
        return APIResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(MessageConstant.ADD_NEW_BRAND_MATERIAL_FAIL)
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
}
