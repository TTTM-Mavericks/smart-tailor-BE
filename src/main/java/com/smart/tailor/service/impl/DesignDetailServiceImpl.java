package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.*;
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
    private static final Double PIXEL_TO_CENTIMETER = 0.0264583333;
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
            /**
             * Create Parent Order
             */
            OrderResponse parentOrderResponse = orderService.createOrder(
                    OrderRequest
                            .builder()
                            .designID(designId)
                            .quantity(0)
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

                parentOrder.setQuantity(parentOrder.getQuantity() + quantity);
                orderService.updateOrder(parentOrder);

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

        // Get All Information Include Width, Height and Material of PartOfDesign of Design
        // Get All Information Include ScaleX, ScaleY and Material of ItemMask of Design
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
                }
        );

        // Loop each SubOrder of ParentOrder
        double totalPriceOfParentOrder = 0;
        double customerPriceDeposit = 0;
        double customerPriceLaborQuantity = 0;
        List<BrandDetailPriceResponse> brandDetailPriceResponseList = new ArrayList<>();
        for (var subOrder : listSubOrders) {
            List<DesignDetail> designDetailList = getDesignDetailBySubOrderID(subOrder.getOrderID());

            // Calculate All Quantity of Each Brand Can Pick
            int totalQuantityOfSubOrder = designDetailList
                    .stream()
                    .mapToInt(DesignDetail::getQuantity)
                    .sum();

            double totalPriceOfEachSubOrder = 0;
            double brandPriceDeposit = 0;
            double brandPriceLaborQuantity = 0;
            Brand brand = null;
            for (DesignDetail designDetail : designDetailList) {
                brand = designDetail.getBrand();
                var designDetailQuantity = designDetail.getQuantity();
                var size = designDetail.getSize();

                // Calculate SizeExpertTailoring of Each SubOrder => Get Radio to Calculate PartOfDesign
                var sizeExpertTailoring = sizeExpertTailoringService.findSizeExpertTailoringByExpertTailoringIDAndSizeID(
                        expertTailoring.getExpertTailoringID(),
                        size.getSizeID()
                );
                var ratio = sizeExpertTailoring.getRatio();

                // Calculate All PartOfDesign Of One Design of Each SubOrder With BrandMaterialPrice And Ratio from SizeExpertTailoring
                var totalPricePartOfDesignOfSubOrder = 0;
                for (PartOfDesignInformation partOfDesignInformation : partOfDesignInformationList) {
                    totalPricePartOfDesignOfSubOrder += calculatePartOfDesignByBrandMaterial(partOfDesignInformation, brand.getBrandID(), ratio);
                }

                // Calculate All ItemMask Of One Design of Each SubOrder With BrandMaterialPrice
                var totalPriceItemMaskOfSubOrder = 0;
                for (ItemMaskInformation itemMaskInformation : itemMaskInformationList) {
                    totalPriceItemMaskOfSubOrder += calculateItemMaskByBrandMaterial(itemMaskInformation, brand.getBrandID());
                }

                // Calculate All Quantity of Each Brand to Get BrandLaborQuantity of Order
                var brandLaborQuantityOfSubOrder = brandLaborQuantityService.findLaborQuantityByBrandIDAndBrandQuantity(brand.getBrandID(), totalQuantityOfSubOrder);

                brandPriceDeposit += (totalPricePartOfDesignOfSubOrder + totalPriceItemMaskOfSubOrder) * designDetailQuantity;
                brandPriceLaborQuantity += brandLaborQuantityOfSubOrder.getLaborCostPerQuantity() * designDetailQuantity;
                customerPriceDeposit += (totalPricePartOfDesignOfSubOrder + totalPriceItemMaskOfSubOrder) * designDetailQuantity;
                customerPriceLaborQuantity += brandLaborQuantityOfSubOrder.getLaborCostPerQuantity() * designDetailQuantity;
                totalPriceOfEachSubOrder += (totalPricePartOfDesignOfSubOrder + totalPriceItemMaskOfSubOrder + brandLaborQuantityOfSubOrder.getLaborCostPerQuantity()) * designDetailQuantity;
            }
            BrandDetailPriceResponse brandDetailPriceResponse = BrandDetailPriceResponse
                    .builder()
                    .brandID(brand.getBrandID())
                    .subOrderID(subOrder.getOrderID())
                    .brandPriceDeposit(brandPriceDeposit)
                    .brandPriceFirstStage(brandPriceLaborQuantity / 2)
                    .brandPriceSecondStage(brandPriceLaborQuantity / 2)
                    .build();
            brandDetailPriceResponseList.add(brandDetailPriceResponse);
            totalPriceOfParentOrder += totalPriceOfEachSubOrder;
        }

        return OrderDetailPriceResponse
                .builder()
                .totalPriceOfParentOrder(totalPriceOfParentOrder)
                .customerPriceDeposit(customerPriceDeposit)
                .customerPriceFirstStage(customerPriceLaborQuantity / 2)
                .customerSecondStage(customerPriceLaborQuantity / 2)
                .brandDetailPriceResponseList(brandDetailPriceResponseList)
                .build();
    }

    private Integer calculatePartOfDesignByBrandMaterial(PartOfDesignInformation partInfo, UUID brandID, Double ratio) {
        var width = partInfo.getWidth() * ratio; // in Centimeter
        var height = partInfo.getHeight() * ratio; // in Centimeter
        var materialID = partInfo.getMaterialID();
        var brandPriceMaterial = brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID);
        var price = (int) Math.ceil(width * height / 10000.0 * brandPriceMaterial);
        return price;
    }

    private Integer calculateItemMaskByBrandMaterial(ItemMaskInformation itemMaskInfo, UUID brandID) {
        var scaleX_Centimeter = Math.abs(itemMaskInfo.getScaleX()) * PIXEL_TO_CENTIMETER;
        var scaleY_Centimeter = Math.abs(itemMaskInfo.getScaleY()) * PIXEL_TO_CENTIMETER;
        var materialID = itemMaskInfo.getMaterialID();
        var brandPriceMaterial = brandMaterialService.getBrandPriceByBrandIDAndMaterialID(brandID, materialID);
        var price = (int) Math.ceil(scaleX_Centimeter * scaleY_Centimeter / 10000.0 * brandPriceMaterial);
        return price;
    }
}
