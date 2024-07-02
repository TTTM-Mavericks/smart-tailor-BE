package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.LaborQuantity;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.LaborQuantityMapper;
import com.smart.tailor.repository.LaborQuantityRepository;
import com.smart.tailor.service.LaborQuantityService;
import com.smart.tailor.utils.request.LaborQuantityRequest;
import com.smart.tailor.utils.response.LaborQuantityResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaborQuantityServiceImpl implements LaborQuantityService {
    private final LaborQuantityRepository laborQuantityRepository;
    private final LaborQuantityMapper laborQuantityMapper;
    private final Logger logger = LoggerFactory.getLogger(LaborQuantityServiceImpl.class);

    @Transactional
    @Override
    public void createLaborQuantity(LaborQuantityRequest laborQuantityRequest) {
        Integer laborQuantityMinQuantity = laborQuantityRequest.getLaborQuantityMinQuantity();
        Integer laborQuantityMaxQuantity = laborQuantityRequest.getLaborQuantityMaxQuantity();
        Double laborQuantityMinPrice = laborQuantityRequest.getLaborQuantityMinPrice();
        Double laborQuantityMaxPrice = laborQuantityRequest.getLaborQuantityMaxPrice();

        if(laborQuantityMinQuantity >= laborQuantityMaxQuantity){
            throw new BadRequestException("Min Quantity Can Not Greater Than Max Quantity");
        }

        if(laborQuantityMinPrice > laborQuantityMaxPrice){
            throw new BadRequestException("Min Price Can Not Greater Than Max Price");
        }

        var laborQuantity = laborQuantityRepository.findByLaborQuantityMinQuantityAndLaborQuantityMaxQuantity(laborQuantityMinQuantity, laborQuantityMaxQuantity);
        if (laborQuantity.isPresent()){
            throw new ItemAlreadyExistException(MessageConstant.LABOR_QUANTITY_IS_EXISTED);
        }

        var checkValidMaxRangeQuantity = laborQuantityRepository.findLaborQuantityByQuantity(laborQuantityMinQuantity);
        var checkValidMinRangeQuantity = laborQuantityRepository.findLaborQuantityByQuantity(laborQuantityMaxQuantity);
        if(!checkValidMaxRangeQuantity.isEmpty() || !checkValidMinRangeQuantity.isEmpty()){
            throw new BadRequestException("Current Range Labor Quantity Can not intersect with Another Range Labor Quantity");
        }

        var laborQuantitySaved = laborQuantityRepository.save(
                LaborQuantity
                        .builder()
                        .laborQuantityMinQuantity(laborQuantityMinQuantity)
                        .laborQuantityMaxQuantity(laborQuantityMaxQuantity)
                        .laborQuantityMinPrice(laborQuantityMinPrice)
                        .laborQuantityMaxPrice(laborQuantityMaxPrice)
                        .status(true)
                        .build()
        );
    }

    @Override
    public List<LaborQuantityResponse> findAllLaborQuantity() {
        return laborQuantityRepository
                .findAll()
                .stream()
                .map(laborQuantityMapper::mapperToLaborQuantityResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateLaborQuantity(UUID laborQuantityID, LaborQuantityRequest laborQuantityRequest) {
        Integer laborQuantityMinQuantity = laborQuantityRequest.getLaborQuantityMinQuantity();
        Integer laborQuantityMaxQuantity = laborQuantityRequest.getLaborQuantityMaxQuantity();
        Double laborQuantityMinPrice = laborQuantityRequest.getLaborQuantityMinPrice();
        Double laborQuantityMaxPrice = laborQuantityRequest.getLaborQuantityMaxPrice();

        var currentLaborQuantity = laborQuantityRepository.findById(laborQuantityID)
                .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_LABOR_QUANTITY));

        if(laborQuantityMinQuantity > laborQuantityMaxQuantity){
            throw new BadRequestException("Min Quantity Can Not Greater Than Max Quantity");
        }

        if(laborQuantityMinPrice > laborQuantityMaxPrice){
            throw new BadRequestException("Min Price Can Not Greater Than Max Price");
        }

        var checkValidMaxRangeQuantity = laborQuantityRepository.findLaborQuantityByQuantity(laborQuantityMinQuantity);
        var checkValidMinRangeQuantity = laborQuantityRepository.findLaborQuantityByQuantity(laborQuantityMaxQuantity);
        var laborQuantityExisted = laborQuantityRepository.findByLaborQuantityMinQuantityAndLaborQuantityMaxQuantity(laborQuantityMinQuantity, laborQuantityMaxQuantity);

        if (laborQuantityExisted.isPresent()){
            if(!laborQuantityExisted.get().getLaborQuantityID().toString().equals(currentLaborQuantity.getLaborQuantityID().toString())) {
                throw new ItemAlreadyExistException(MessageConstant.LABOR_QUANTITY_IS_EXISTED);
            }
        } else {
            if(!checkValidMaxRangeQuantity.isEmpty() || !checkValidMinRangeQuantity.isEmpty()){
                throw new BadRequestException("Current Range Labor Quantity Can not intersect with Another Range Labor Quantity");
            }
        }

        var laborQuantityUpdated = laborQuantityRepository.save(
                LaborQuantity
                        .builder()
                        .laborQuantityID(currentLaborQuantity.getLaborQuantityID())
                        .laborQuantityMinQuantity(laborQuantityMinQuantity)
                        .laborQuantityMaxQuantity(laborQuantityMaxQuantity)
                        .laborQuantityMinPrice(laborQuantityMinPrice)
                        .laborQuantityMaxPrice(laborQuantityMaxPrice)
                        .status(currentLaborQuantity.getStatus())
                        .build()
        );
    }

    @Override
    public Optional<LaborQuantity> findByID(UUID laborQuantityID) {
        return laborQuantityRepository.findById(laborQuantityID);
    }
}
