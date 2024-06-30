package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Discount;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.mapper.DiscountMapper;
import com.smart.tailor.repository.DiscountRepository;
import com.smart.tailor.service.DiscountService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.DiscountRequest;
import com.smart.tailor.utils.response.DiscountResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;
    private final Logger logger = LoggerFactory.getLogger(DiscountServiceImpl.class);

    @Override
    public void addNewDiscount(DiscountRequest discountRequest) {
        try {

            if (discountRequest == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }

            if (discountRequest.getDiscountName() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": discountName");
            }
            String discountName = discountRequest.getDiscountName();
            if (!Utilities.isStringNotNullOrEmpty(discountName)) {
                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": discountName");
            }

            if (discountRequest.getDiscountPercent() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": discountPercent");
            }
            Double discountPercent = discountRequest.getDiscountPercent();
            if (!Utilities.isValidDouble(discountPercent.toString())) {
                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": discountPercent");
            }

            if (discountRequest.getQuantity() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": quantity");
            }
            Integer quantity = discountRequest.getQuantity();
            if (!Utilities.isValidNumber(quantity.toString())) {
                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": quantity");
            }

            LocalDateTime startDateTime;
            if (discountRequest.getStartDateTime() == null) {
                startDateTime = LocalDateTime.now();
            } else {
                startDateTime = discountRequest.getStartDateTime();
            }

            LocalDateTime expiredDateTime;
            if (discountRequest.getExpiredDateTime() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": expiredDateTime");
            } else {
                expiredDateTime = discountRequest.getExpiredDateTime();
            }

            Discount discount = Discount.builder()
                    .discountName(discountName)
                    .discountPercent(discountPercent)
                    .quantity(quantity)
                    .startDateTime(startDateTime)
                    .expiredDateTime(expiredDateTime)
                    .discountStatus(true)
                    .build();
            discountRepository.save(discount);
        } catch (Exception ex) {
            logger.error("ERROR IN DISCOUNT SERVICE: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<DiscountResponse> getAllValidDiscount() {
        try {
            List<DiscountResponse> responseList = null;

            List<Discount> discountList = discountRepository.getAllValidDiscount();
            if (!discountList.isEmpty()) {
                for (Discount discount : discountList) {
                    if (responseList == null) {
                        responseList = new ArrayList<>();
                    }
                    responseList.add(discountMapper.mapToDiscountResponse(discount));
                }
            }

            return responseList;
        } catch (Exception ex) {
            logger.error("ERROR IN DISCOUNT SERVICE: {}", ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<DiscountResponse> getAllDiscount() {
        try {
            List<DiscountResponse> responseList = null;

            List<Discount> discountList = discountRepository.findAll();
            if (!discountList.isEmpty()) {
                for (Discount discount : discountList) {
                    if (responseList == null) {
                        responseList = new ArrayList<>();
                    }
                    responseList.add(discountMapper.mapToDiscountResponse(discount));
                }
            }

            return responseList;
        } catch (Exception ex) {
            logger.error("ERROR IN DISCOUNT SERVICE: {}", ex.getMessage());
            throw ex;
        }
    }
}
