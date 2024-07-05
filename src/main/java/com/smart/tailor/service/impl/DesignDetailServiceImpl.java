package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.entities.Order;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.DesignDetailMapper;
import com.smart.tailor.repository.DesignDetailRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.DesignDetailRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignDetailResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DesignDetailServiceImpl implements DesignDetailService {

    private final DesignDetailRepository designDetailRepository;
    private final DesignDetailMapper designDetailMapper;
    private final BrandService brandService;
    private final DesignService designService;
    private final OrderService orderService;
    private final SizeService sizeService;
    private final Logger logger = LoggerFactory.getLogger(DesignDetailServiceImpl.class);

    @Override
    public List<DesignDetailResponse> getAllByDesignID(UUID designID) {
        try {
            if (designID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }
            List<DesignDetail> designDetailList = designDetailRepository.findAllByDesignDesignID(designID);
            List<DesignDetailResponse> responseList = null;
            if (!designDetailList.isEmpty()) {
                for (DesignDetail detail : designDetailList) {
                    if (responseList == null) {
                        responseList = new ArrayList<>();
                    }
                    responseList.add(designDetailMapper.mapperToDesignDetailResponse(detail));
                }
            }
            return responseList;
        } catch (Exception ex) {
            logger.error("ERROR IN DESIGN DETAIL SERVICE: {}", ex.getMessage());
            return null;
        }
    }

    @Transactional
    @Override
    public APIResponse createDesignDetail(DesignDetailRequest designDetailRequest) {
        try {
            if (designDetailRequest == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }

            if (designDetailRequest.getDesignId() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designId");
            }
            UUID designId = designDetailRequest.getDesignId();

            if (designDetailRequest.getSizeID() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": size");
            }
//            String sizeID = designDetailRequest.getSize();
//            if (!Utilities.isStringNotNullOrEmpty(size)) {
//                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": size");
//            }

            if (designDetailRequest.getQuantity() == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": quantity");
            }
            Integer quantity = designDetailRequest.getQuantity();
            if (!Utilities.isValidNumber(quantity.toString()) || quantity <= 0) {
                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": quantity");
            }

            Design design = designService.getDesignByID(designId);
            var brand = brandService.getBrandById(designDetailRequest.getBrandId());
            Brand existedBrand;
            if (brand.isEmpty()) {
                existedBrand = null;
            } else {
                existedBrand = brand.get();
            }

            var order = orderService.getOrderById(designDetailRequest.getOrderId());
            Order existedOrder;
            if (order.isEmpty()) {
                existedOrder = null;
            } else {
                existedOrder = order.get();
            }

            var size = sizeService.findByID(UUID.fromString(designDetailRequest.getSizeID()))
                    .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE));

            DesignDetail detail = DesignDetail
                    .builder()
                    .design(design)
                    .brand(existedBrand)
                    .order(existedOrder)
                    .size(size)
                    .quantity(quantity)
                    .detailStatus(true)
                    .build();

            designDetailRepository.save(detail);
            DesignDetailResponse detailResponse = getDesignDetailByDesignAndSize(designId, size.getSizeID());

            if (detailResponse != null) {
                APIResponse
                        .builder()
                        .status(HttpStatus.OK.value())
                        .message(MessageConstant.ADD_NEW_DESIGN_DETAIL_SUCCESSFULLY)
                        .data(detailResponse)
                        .build();
            }
        } catch (Exception ex) {
            logger.error("ERROR IN DESIGN DETAIL SERVICE: {}", ex.getMessage());
        }
        return null;
    }

    @Override
    public DesignDetailResponse getDesignDetailByDesignAndSize(UUID designID, UUID sizeID) {
        try {
            if (designID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designID");
            }

            if (sizeID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": size");
            }
            if (!Utilities.isStringNotNullOrEmpty(sizeID.toString())) {
                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": size");
            }

            return designDetailMapper.mapperToDesignDetailResponse(
                    designDetailRepository.findDesignDetailByDesignDesignIDAndSizeSizeID(
                            designID,
                            sizeID
                    )
            );
        } catch (Exception ex) {
            logger.error("ERROR IN DESIGN DETAIL SERVICE: {}", ex.getMessage());
            return null;
        }
    }
}
