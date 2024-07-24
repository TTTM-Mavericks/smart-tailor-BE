package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.BrandProperties;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.mapper.BrandPropertiesMapper;
import com.smart.tailor.repository.BrandPropertiesRepository;
import com.smart.tailor.service.BrandPropertiesService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.SystemPropertiesService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.BrandPropertiesRequest;
import com.smart.tailor.utils.response.BrandPropertiesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandPropertiesServiceImpl implements BrandPropertiesService {
    private final SystemPropertiesService systemService;
    private final BrandService brandService;
    private final BrandPropertiesRepository brandPropertiesRepository;
    private final BrandPropertiesMapper brandPropertiesMapper;

    @Override
    public List<BrandPropertiesResponse> getAllByBrandID(UUID brandID) {
        try {
            if (brandID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            return brandPropertiesRepository.getAllByBrand_BrandID(brandID).stream().map(brandPropertiesMapper::mapperToBrandPropertiesResponse).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<BrandPropertiesResponse> getAll() {
        try {
            return brandPropertiesRepository.findAll().stream().map(brandPropertiesMapper::mapperToBrandPropertiesResponse).toList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public BrandPropertiesResponse getByID(UUID propertyID) {
        try {
            if (propertyID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            var property = brandPropertiesRepository.findById(propertyID);
            if (property.isEmpty()) {
                return null;
            }
            return brandPropertiesMapper.mapperToBrandPropertiesResponse(property.get());
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional
    @Override
    public BrandPropertiesResponse addNew(BrandPropertiesRequest brandRequest) throws Exception {
        try {
            UUID brandID = UUID.fromString(brandRequest.getBrandID());

            var brand = brandService.getBrandById(brandID);
            if (brand.isEmpty()) {
                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND);
            }

            UUID systemPropertyID = UUID.fromString(brandRequest.getSystemPropertyID());
            var systemProperty = systemService.getObjectByID(systemPropertyID);
            if (systemProperty.isEmpty()) {
                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_SYSTEM_PROPERTY);
            }

            String brandPropertyValue = brandRequest.getBrandPropertyValue().trim().toUpperCase();
            Boolean brandPropertyStatus = brandRequest.getBrandPropertyStatus() != null ? brandRequest.getBrandPropertyStatus() : true;

            var newBrandProperties = brandPropertiesRepository.save(
                    BrandProperties
                            .builder()
                            .systemProperties(systemProperty.get())
                            .brand(brand.get())
                            .brandPropertyValue(brandPropertyValue)
                            .brandPropertyStatus(brandPropertyStatus)
                            .build()
            );
            if (newBrandProperties == null) {
                return null;
            }
            return brandPropertiesMapper.mapperToBrandPropertiesResponse(newBrandProperties);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
