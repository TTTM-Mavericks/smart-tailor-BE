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
import com.smart.tailor.utils.request.DesignDetailSize;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.response.APIResponse;
import com.smart.tailor.utils.response.DesignDetailResponse;
import com.smart.tailor.utils.response.OrderDetailResponse;
import com.smart.tailor.utils.response.OrderResponse;
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
            Design design = designService.getDesignByID(designId);
            if (design == null) {
                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN + " with id: " + designId);
            }
            Order existedOrder = null;
//            if (designDetailRequest.getOrderId() != null) {
//                var order = orderService.getOrderById(designDetailRequest.getOrderId());
//                if (order.isEmpty()) {
//                    throw new BadRequestException("CAN NOT FIND ORDER BY ODER ID.");
//                } else {
//                    existedOrder = order.get();
//                }
//            } else {
                OrderResponse createdOrder = orderService.createOrder(
                        OrderRequest
                                .builder()
                                .designID(designId)
                                .orderType("PARENT_ORDER")
                                .build()
                );
                existedOrder = orderService.getOrderById(createdOrder.getOrderID()).get();
//            }

            List<DesignDetailSize> sizeList = designDetailRequest.getSizeList();
            int index = -1;
            List<DesignDetail> designDetailList = new ArrayList<>();
            for (DesignDetailSize sizeRequest : sizeList) {
                index++;
                if (sizeRequest.getSizeID() == null) {
                    throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " at [" + index + "]: sizeID");
                }
                var size = sizeService.findByID(UUID.fromString(sizeRequest.getSizeID()))
                        .orElseThrow(() -> new ItemNotFoundException(MessageConstant.CAN_NOT_FIND_ANY_SIZE));

                if (sizeRequest.getQuantity() == null) {
                    throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " at [" + index + "]: quantity");
                }
                Integer quantity = sizeRequest.getQuantity();
                if (!Utilities.isValidNumber(quantity.toString()) || quantity <= 0) {
                    throw new BadRequestException(MessageConstant.INVALID_INPUT + " at [" + index + "]: quantity");
                }

                Brand existedBrand = null;
                if (sizeRequest.getBrandId() != null) {
                    var brand = brandService.getBrandById(sizeRequest.getBrandId());
                    if (brand.isEmpty()) {
                        existedBrand = null;
                    } else {
                        existedBrand = brand.get();
                    }
                }
                existedOrder.setQuantity(existedOrder.getQuantity() + quantity);
                orderService.updateOrder(existedOrder);
                designDetailList.add(
                        DesignDetail
                                .builder()
                                .design(design)
                                .brand(existedBrand)
                                .order(existedOrder)
                                .size(size)
                                .quantity(quantity)
                                .detailStatus(true)
                                .build()
                );

            }

            designDetailRepository.saveAll(designDetailList);

            return APIResponse
                    .builder()
                    .status(HttpStatus.OK.value())
                    .message(MessageConstant.ADD_NEW_DESIGN_DETAIL_SUCCESSFULLY)
                    .data(
                            OrderDetailResponse.builder()
                                    .sizeList(sizeList)
                                    .orderID(existedOrder.getOrderID())
                                    .build()
                    )
                    .build();

        } catch (
                Exception ex) {
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

    @Override
    public DesignDetailResponse updateDesignDetail(DesignDetail designDetail) {
        return designDetailMapper.mapperToDesignDetailResponse(designDetailRepository.save(designDetail));
    }
}
