package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Brand;
import com.smart.tailor.entities.Design;
import com.smart.tailor.entities.DesignDetail;
import com.smart.tailor.entities.Order;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.DesignDetailMapper;
import com.smart.tailor.mapper.DesignMapper;
import com.smart.tailor.mapper.OrderMapper;
import com.smart.tailor.repository.DesignDetailRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.*;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DesignDetailServiceImpl implements DesignDetailService {

    private final DesignDetailRepository designDetailRepository;
    private final DesignDetailMapper designDetailMapper;
    private final DesignMapper designMapper;
    private final OrderMapper orderMapper;
    private final BrandService brandService;
    private final BrandMaterialService brandMaterialService;
    private final SizeExpertTailoringService sizeExpertTailoringService;
    private final DesignService designService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final SizeService sizeService;
    private final BrandLaborQuantityService brandLaborQuantityService;
    private static final BigDecimal PIXEL_TO_CENTIMETER = new BigDecimal("0.0264583");
    private final Logger logger = LoggerFactory.getLogger(DesignDetailServiceImpl.class);

    @Transactional(readOnly = true)
    @Override
    public DesignDetailCustomResponse findAllByOrderID(UUID orderID) {
        try {
            if (orderID == null) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT);
            }

            DesignDetailCustomResponse responseList = new DesignDetailCustomResponse();
//            List<DesignDetailResponse> detailList = null;
//            if (!designDetailList.isEmpty()) {
//                for (DesignDetail detail : designDetailList) {
//                    if (detailList == null) {
//                        detailList = new ArrayList<>();
//                    }
//                    detailList.add(designDetailMapper.mapperToDesignDetailResponse(detail));
//                }
//            }
            var designID = designService.getDesignObjectByOrderID(orderID).getDesignID();
            var designResponse = designService.getDesignResponseByID(designID);
            responseList.setDesign(designResponse);
            var order = orderService.getOrderById(orderID).get();
            responseList.setOrder(orderMapper.mapToOrderResponse(order));
            List<DesignDetail> detailList = designDetailRepository.findAllByOrderID(orderID);
            if (detailList != null) {
                responseList.setDesignDetail(detailList.stream().map(designDetailMapper::mapperToDesignDetailResponse).toList());
            }
            return responseList;
        } catch (Exception ex) {
            logger.error("ERROR IN DESIGN DETAIL SERVICE: {}", ex.getMessage());
            return null;
        }
    }

    @Override
    public DesignDetailResponse findByID(UUID orderID) {
        return designDetailMapper.mapperToDesignDetailResponse(
                designDetailRepository.getDesignDetailByDesignDetailID(orderID).get()
        );
    }

    //    @Transactional(readOnly = true)
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
            Order parentOrder = null;
            DesignResponse designResponse = designService.getDesignResponseByID(designId);
            UserResponse userResponse = designResponse.getUser();
            CustomerResponse customerResponse = customerService.getCustomerByUserID(userResponse.getUserID());
            String address = "";
            String province = "";
            String district = "";
            String ward = "";
            if (Utilities.isNonNullOrEmpty(designDetailRequest.getAddress())
                    && Utilities.isNonNullOrEmpty(designDetailRequest.getProvince())
                    && Utilities.isNonNullOrEmpty(designDetailRequest.getDistrict())
                    && Utilities.isNonNullOrEmpty(designDetailRequest.getWard())) {
                address = designDetailRequest.getAddress();
                province = designDetailRequest.getProvince();
                district = designDetailRequest.getDistrict();
                ward = designDetailRequest.getWard();
            } else {
                address = customerResponse.getAddress();
                province = customerResponse.getProvince();
                district = customerResponse.getDistrict();
                ward = customerResponse.getWard();
            }

            String phone = "";
            if (!Utilities.isNonNullOrEmpty(designDetailRequest.getPhone())) {
                phone = customerResponse.getPhoneNumber();
            } else if (!Utilities.isValidVietnamesePhoneNumber(designDetailRequest.getPhone())) {
                phone = customerResponse.getPhoneNumber();
            } else {
                phone = designDetailRequest.getPhone();
            }

            String buyerName;
            if (Utilities.isNonNullOrEmpty(designDetailRequest.getBuyerName())) {
                buyerName = designDetailRequest.getBuyerName();
            } else {
                buyerName = customerResponse.getFullName();
            }
            List<DesignDetailSize> sizeList = designDetailRequest.getSizeList();
            int index = -1;
            int totalQuantity = 0;
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
                totalQuantity += quantity;
            }
            /**
             * Create Parent Order
             */
            OrderResponse parentOrderResponse = orderService.createOrder(
                    OrderRequest
                            .builder()
                            .designID(designId)
                            .quantity(totalQuantity)
                            .parentOrderID(null)
                            .orderType("PARENT_ORDER")
                            .address(address)
                            .province(province)
                            .district(district)
                            .ward(ward)
                            .phone(phone)
                            .buyerName(buyerName)
                            .orderStatus(OrderStatus.NOT_VERIFY)
                            .build()
            );
            logger.info("CREATE NEW ORDER SUCCESSFULLY!: {}", parentOrderResponse);
            parentOrder = orderService.getOrderById(parentOrderResponse.getOrderID()).get();

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

//                parentOrder.setQuantity(parentOrder.getQuantity() + quantity);
//                orderService.updateOrder(parentOrder);

                designDetailList.add(
                        DesignDetail
                                .builder()
                                .design(design)
                                .brand(null)
                                .order(parentOrder)
                                .size(size)
                                .quantity(quantity)
                                .detailStatus(false)
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
                                    .orderID(parentOrder.getOrderID())
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
    public DesignDetailResponse updateDesignDetail(DesignDetail designDetail) {
        return designDetailMapper.mapperToDesignDetailResponse(designDetailRepository.save(designDetail));
    }

    @Override
    public DesignDetail updateDetailByID(DesignDetail designDetail) {
        return designDetailRepository.save(designDetail);
    }

    @Override
    public Optional<DesignDetail> getDesignDetailObjectByID(UUID detailID) {
        return designDetailRepository.findById(detailID);
    }

    @Override
    public DesignDetail getDetailOfOrderBaseOnBrandID(UUID orderID, UUID brandID) {
        return designDetailRepository.getDetailOfOrderBaseOnBrandID(orderID, brandID);
    }

    @Override
    public List<DesignDetail> getDesignDetailBySubOrderID(UUID subOrderID) {
        return designDetailRepository.getDesignDetailBySubOrderID(subOrderID);
    }

    @Override
    public OrderDetailPriceResponse calculateTotalPriceForSpecificOrder(UUID parentOrderID) throws Exception {
        var orderCustomResponse = orderService.getOrderByOrderID(parentOrderID);
        var listSubOrders = orderService.getSubOrderByParentID(parentOrderID);
        var designResponse = orderCustomResponse.getDesignResponse();
        var expertTailoring = designResponse.getExpertTailoring();
        List<PartOfDesignInformation> partOfDesignInformationList = new ArrayList<>();
        List<ItemMaskInformation> itemMaskInformationList = new ArrayList<>();

        designResponse.getPartOfDesign().forEach(partOfDesignResponse -> {
            partOfDesignInformationList.add(PartOfDesignInformation
                    .builder()
                    .width(partOfDesignResponse.getWidth())
                    .height(partOfDesignResponse.getHeight())
                    .materialID(partOfDesignResponse.getMaterial().getMaterialID())
                    .materialName(partOfDesignResponse.getMaterial().getMaterialName())
                    .build());
            partOfDesignResponse.getItemMasks().forEach(itemMaskResponse -> {
                itemMaskInformationList.add(ItemMaskInformation
                        .builder()
                        .scaleX(itemMaskResponse.getScaleX())
                        .scaleY(itemMaskResponse.getScaleY())
                        .materialID(itemMaskResponse.getMaterial().getMaterialID())
                        .materialName(itemMaskResponse.getMaterial().getMaterialName())
                        .build());
            });
        });

        BigDecimal totalPriceOfParentOrder = BigDecimal.ZERO;
        BigDecimal customerPriceDeposit = BigDecimal.ZERO;
        BigDecimal customerPriceLaborQuantity = BigDecimal.ZERO;
        List<BrandDetailPriceResponse> brandDetailPriceResponseList = new ArrayList<>();
        for (var subOrder : listSubOrders) {
            List<DesignDetail> designDetailList = getDesignDetailBySubOrderID(subOrder.getOrderID());

            int totalQuantityOfSubOrder = designDetailList
                    .stream()
                    .mapToInt(DesignDetail::getQuantity)
                    .sum();

            BigDecimal totalPriceOfEachSubOrder = BigDecimal.ZERO;
            BigDecimal brandPriceDeposit = BigDecimal.ZERO;
            BigDecimal brandPriceLaborQuantity = BigDecimal.ZERO;
            Brand brand = null;
            for (DesignDetail designDetail : designDetailList) {
                brand = designDetail.getBrand();
                var designDetailQuantity = BigDecimal.valueOf(designDetail.getQuantity());
                var size = designDetail.getSize();

                var sizeExpertTailoring = sizeExpertTailoringService.findSizeExpertTailoringByExpertTailoringIDAndSizeID(
                        expertTailoring.getExpertTailoringID(),
                        size.getSizeID()
                );
                var ratio = BigDecimal.valueOf(sizeExpertTailoring.getRatio());

                BigDecimal totalPricePartOfDesignOfSubOrder = BigDecimal.ZERO;
                for (PartOfDesignInformation partOfDesignInformation : partOfDesignInformationList) {
                    totalPricePartOfDesignOfSubOrder = totalPricePartOfDesignOfSubOrder.add(calculatePartOfDesignByBrandMaterial(partOfDesignInformation, brand.getBrandID(), ratio));
                }

                BigDecimal totalPriceItemMaskOfSubOrder = BigDecimal.ZERO;
                for (ItemMaskInformation itemMaskInformation : itemMaskInformationList) {
                    totalPriceItemMaskOfSubOrder = totalPriceItemMaskOfSubOrder.add(calculateItemMaskByBrandMaterial(itemMaskInformation, brand.getBrandID()));
                }

                var brandLaborQuantityOfSubOrder = brandLaborQuantityService.findLaborQuantityByBrandIDAndBrandQuantity(brand.getBrandID(), totalQuantityOfSubOrder);

                BigDecimal brandLaborCostPerQuantity = BigDecimal.valueOf(brandLaborQuantityOfSubOrder.getLaborCostPerQuantity());

                brandPriceDeposit = brandPriceDeposit.add(totalPricePartOfDesignOfSubOrder.add(totalPriceItemMaskOfSubOrder).multiply(designDetailQuantity));
                brandPriceLaborQuantity = brandPriceLaborQuantity.add(brandLaborCostPerQuantity.multiply(designDetailQuantity));
                customerPriceDeposit = customerPriceDeposit.add(totalPricePartOfDesignOfSubOrder.add(totalPriceItemMaskOfSubOrder).multiply(designDetailQuantity));
                customerPriceLaborQuantity = customerPriceLaborQuantity.add(brandLaborCostPerQuantity.multiply(designDetailQuantity));
                totalPriceOfEachSubOrder = totalPriceOfEachSubOrder.add(totalPricePartOfDesignOfSubOrder.add(totalPriceItemMaskOfSubOrder).add(brandLaborCostPerQuantity).multiply(designDetailQuantity));
            }
            BrandDetailPriceResponse brandDetailPriceResponse = BrandDetailPriceResponse
                    .builder()
                    .brandID(brand.getBrandID())
                    .subOrderID(subOrder.getOrderID())
                    .brandPriceDeposit(brandPriceDeposit.toString())
                    .brandPriceFirstStage(brandPriceLaborQuantity.divide(BigDecimal.valueOf(2)).toString())
                    .brandPriceSecondStage(brandPriceLaborQuantity.divide(BigDecimal.valueOf(2)).toString())
                    .build();
            brandDetailPriceResponseList.add(brandDetailPriceResponse);
            totalPriceOfParentOrder = totalPriceOfParentOrder.add(totalPriceOfEachSubOrder);
        }

        return OrderDetailPriceResponse
                .builder()
                .totalPriceOfParentOrder(totalPriceOfParentOrder.toString())
                .customerPriceDeposit(customerPriceDeposit.toString())
                .customerPriceFirstStage(customerPriceLaborQuantity.divide(BigDecimal.valueOf(2)).toString())
                .customerSecondStage(customerPriceLaborQuantity.divide(BigDecimal.valueOf(2)).toString())
                .brandDetailPriceResponseList(brandDetailPriceResponseList)
                .build();
    }


    private BigDecimal calculatePartOfDesignByBrandMaterial(PartOfDesignInformation partInfo, UUID brandID, BigDecimal ratio) {
        BigDecimal width = BigDecimal.valueOf(partInfo.getWidth()).multiply(ratio); // in Centimeter
        BigDecimal height = BigDecimal.valueOf(partInfo.getHeight()).multiply(ratio); // in Centimeter
        UUID materialID = partInfo.getMaterialID();
        BigDecimal brandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID));
        BigDecimal area = width.multiply(height).divide(BigDecimal.valueOf(10000), BigDecimal.ROUND_CEILING);
        BigDecimal price = area.multiply(brandPriceMaterial).setScale(0, BigDecimal.ROUND_CEILING);
        return price;
    }

    private BigDecimal calculateItemMaskByBrandMaterial(ItemMaskInformation itemMaskInfo, UUID brandID) {
        BigDecimal scaleX_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleX())).multiply(PIXEL_TO_CENTIMETER);
        BigDecimal scaleY_Centimeter = BigDecimal.valueOf(Math.abs(itemMaskInfo.getScaleY())).multiply(PIXEL_TO_CENTIMETER);
        UUID materialID = itemMaskInfo.getMaterialID();
        BigDecimal brandPriceMaterial = BigDecimal.valueOf(brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID));
        BigDecimal area = scaleX_Centimeter.multiply(scaleY_Centimeter).divide(BigDecimal.valueOf(10000), BigDecimal.ROUND_CEILING);
        BigDecimal price = area.multiply(brandPriceMaterial).setScale(0, BigDecimal.ROUND_CEILING);
        return price;
    }
}
