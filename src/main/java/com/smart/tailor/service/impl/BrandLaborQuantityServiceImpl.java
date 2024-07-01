package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.BrandLaborQuantity;
import com.smart.tailor.entities.BrandLaborQuantityKey;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.BrandLaborQuantityMapper;
import com.smart.tailor.repository.BrandExpertTailoringRepository;
import com.smart.tailor.repository.BrandLaborQuantityRepository;
import com.smart.tailor.service.BrandLaborQuantityService;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.LaborQuantityService;
import com.smart.tailor.utils.request.BrandLaborQuantityListRequest;
import com.smart.tailor.utils.request.BrandLaborQuantityRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.BrandLaborQuantityResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandLaborQuantityServiceImpl implements BrandLaborQuantityService {
    private final BrandLaborQuantityRepository brandLaborQuantityRepository;
    private final BrandLaborQuantityMapper brandLaborQuantityMapper;
    private final BrandService brandService;
    private final LaborQuantityService laborQuantityService;
    private final Logger logger = LoggerFactory.getLogger(BrandLaborQuantityServiceImpl.class);

    @Transactional
    @Override
    public void createBrandLaborQuantity(UUID brandID, BrandLaborQuantityListRequest brandLaborQuantityListRequest) {
        var brandExisted = brandService.findBrandById(brandID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_BRAND));

        var brandLaborQuantityRequests = brandLaborQuantityListRequest.getBrandLaborQuantity();
        for(BrandLaborQuantityRequest brandLaborQuantityRequest : brandLaborQuantityRequests){
            var laborQuantityID = UUID.fromString(brandLaborQuantityRequest.getLaborQuantityID());
            var laborQuantity = laborQuantityService.findByID(laborQuantityID)
                    .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_LABOR_QUANTITY));

            if(
                    laborQuantity.getLaborQuantityMinPrice() > brandLaborQuantityRequest.getBrandLaborCostPerQuantity() ||
                    laborQuantity.getLaborQuantityMaxPrice() < brandLaborQuantityRequest.getBrandLaborCostPerQuantity()
            ){
                throw new BadRequestException("Brand Labor Cost must be between Min and Max Price");
            }

            BrandLaborQuantityKey brandLaborQuantityKey = BrandLaborQuantityKey
                    .builder()
                    .laborQuantityID(laborQuantityID)
                    .brandID(brandID)
                    .build();

            BrandLaborQuantity brandLaborQuantity = BrandLaborQuantity
                    .builder()
                    .brandLaborQuantityKey(brandLaborQuantityKey)
                    .brand(brandExisted)
                    .laborQuantity(laborQuantity)
                    .brandLaborCostPerQuantity(brandLaborQuantityRequest.getBrandLaborCostPerQuantity())
                    .status(true)
                    .build();

            brandLaborQuantityRepository.save(brandLaborQuantity);
        }
    }

    @Override
    public List<BrandLaborQuantityResponse> findBrandLaborQuantityByBrandID(UUID brandID) {
        return brandLaborQuantityRepository
                .findAll()
                .stream()
                .filter(brandLaborQuantity -> brandLaborQuantity.getBrand().getBrandID().toString().equals(brandID.toString()))
                .map(brandLaborQuantityMapper::mapToLaborQuantityResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBrandLaborQuantity(UUID brandID, BrandLaborQuantityListRequest brandLaborQuantityListRequest) {
    }
}
