package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.LaborQuantity;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.DuplicateDataException;
import com.smart.tailor.exception.ItemAlreadyExistException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.LaborQuantityMapper;
import com.smart.tailor.repository.LaborQuantityRepository;
import com.smart.tailor.service.LaborQuantityService;
import com.smart.tailor.utils.request.LaborQuantityRequest;
import com.smart.tailor.utils.request.LaborQuantityRequestList;
import com.smart.tailor.utils.response.LaborQuantityResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void createLaborQuantity(LaborQuantityRequestList laborQuantityRequestList) {
        List<Object> duplicateData = new ArrayList<>();
        if(!checkValidLaborQuantityRange(laborQuantityRequestList.getLaborQuantityRequests())){
            throw new BadRequestException("Labor Quantity Can not intersect with Another Range Labor Quantity");
        }
        for(LaborQuantityRequest laborQuantityRequest : laborQuantityRequestList.getLaborQuantityRequests()){
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
                duplicateData.add(laborQuantityRequest);
                continue;
            }
            if (!duplicateData.isEmpty()) continue;

            laborQuantityRepository.save(
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

        if (!duplicateData.isEmpty()){
            throw new DuplicateDataException(MessageConstant.LABOR_QUANTITY_IS_EXISTED, duplicateData);
        }
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

        laborQuantityRepository.save(
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

    private boolean checkValidLaborQuantityRange(List<LaborQuantityRequest> laborQuantityRequests){
        boolean check = true;
        for(int i = 1; i < laborQuantityRequests.size(); ++i){
            Integer prev_l = laborQuantityRequests.get(i - 1).getLaborQuantityMinQuantity();
            Integer prev_r = laborQuantityRequests.get(i - 1).getLaborQuantityMaxQuantity();
            Integer curr_l = laborQuantityRequests.get(i).getLaborQuantityMinQuantity();
            Integer curr_r = laborQuantityRequests.get(i).getLaborQuantityMaxQuantity();
            if(prev_r < curr_l || prev_l > curr_r) continue;
            else check = false;
        }
        return check;
    }
}
