package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.*;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.DesignDetailMapper;
import com.smart.tailor.mapper.OrderMapper;
import com.smart.tailor.mapper.PaymentMapper;
import com.smart.tailor.repository.DesignDetailRepository;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.OrderPickingRequest;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BrandService brandService;
    private final DesignService designService;
    private final UserService userService;
    private final CustomerService customerService;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;
    private final DesignDetailMapper detailMapper;
    private final BrandMaterialService brandMaterialService;
    private final DesignDetailRepository detailRepository;
    private final PaymentService paymentService;
    private final SystemPropertiesService systemPropertiesService;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) throws Exception {
        try {
            if (!Utilities.isStringNotNullOrEmpty(orderRequest.getDesignID().toString())) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designID");
            }
            UUID designID = orderRequest.getDesignID();

            DesignResponse designResponse = designService.getDesignResponseByID(designID);
            if (designResponse == null) {
                throw new BadRequestException(MessageConstant.INVALID_INPUT + ": designID");
            }

            Integer quantity = orderRequest.getQuantity() != null
                    && Utilities.isValidNumber(orderRequest.getQuantity().toString())
                    ? orderRequest.getQuantity()
                    : 0;

            UserResponse userResponse = designResponse.getUser();
            CustomerResponse customerResponse = customerService.getCustomerByUserID(userResponse.getUserID());

            String address = "";
            String province = "";
            String district = "";
            String ward = "";
            if (Utilities.isNonNullOrEmpty(orderRequest.getAddress())
                    && Utilities.isNonNullOrEmpty(orderRequest.getProvince())
                    && Utilities.isNonNullOrEmpty(orderRequest.getDistrict())
                    && Utilities.isNonNullOrEmpty(orderRequest.getWard())) {
                address = orderRequest.getAddress();
                province = orderRequest.getProvince();
                district = orderRequest.getDistrict();
                ward = orderRequest.getWard();
            } else {
                address = customerResponse.getAddress();
                province = customerResponse.getProvince();
                district = customerResponse.getDistrict();
                ward = customerResponse.getWard();
            }

            String phone = "";
            if (!Utilities.isStringNotNullOrEmpty(orderRequest.getPhone())) {
                phone = customerResponse.getPhoneNumber();
            } else if (!Utilities.isValidVietnamesePhoneNumber(orderRequest.getPhone())) {
                phone = customerResponse.getPhoneNumber();
            } else {
                phone = orderRequest.getPhone();
            }

            String buyerName;
            if (Utilities.isNonNullOrEmpty(orderRequest.getBuyerName())) {
                buyerName = orderRequest.getBuyerName();
            } else {
                buyerName = customerResponse.getFullName();
            }

            String orderType = orderRequest.getOrderType();

            if (orderRequest.getParentOrderID() != null) {
                var parentOrderID = orderRequest.getParentOrderID();
                Optional<Order> parentOrder = orderRepository.findById(parentOrderID);
                if (parentOrder.isPresent()) {
                    Order order = Order.builder()
                            .quantity(quantity)
                            .address(address)
                            .province(province)
                            .district(district)
                            .ward(ward)
                            .orderType(orderType)
                            .phone(phone)
                            .buyerName(buyerName)
                            .orderStatus(orderRequest.getOrderStatus())
                            .orderType("SUB_ORDER")
                            .parentOrder(parentOrder.get())
                            .totalPrice(0)
                            /**
                             * TODO
                             * .employee()
                             */
                            .build();
                    var orderResponse = orderRepository.save(order);
                    return orderMapper.mapToOrderResponse(orderResponse);
                } else {
                    throw new RuntimeException("Parent order not found");
                }
            }
            Order order = Order.builder()
                    .quantity(quantity)
                    .address(address)
                    .province(province)
                    .district(district)
                    .ward(ward)
                    .orderType(orderType)
                    .phone(phone)
                    .buyerName(buyerName)
                    .orderStatus(orderRequest.getOrderStatus())
                    /**
                     * TODO
                     * .employee()
                     */
                    .totalPrice(0)
                    .orderType("PARENT_ORDER")
                    .build();
            var orderResponse = orderRepository.save(order);
            return orderMapper.mapToOrderResponse(orderResponse);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<OrderResponse> getParentOrderByDesignID(UUID designID) throws Exception {
        List<Order> orderList = orderRepository.findParentOrderByDesignID(designID);
        List<OrderResponse> orderResponse = new ArrayList<>();
        for (Order order : orderList) {
            var response = orderMapper.mapToOrderResponse(order);
            orderResponse.add(response);
        }
        ;
        return orderResponse;
    }

    @Override
    public void updateOrderStatus(UUID orderID, String orderStatus) {

    }

    @Override
    public OrderCustomResponse getOrderByOrderID(UUID orderID) throws Exception {
        try {

            var order = orderRepository.findById(orderID).isPresent() ? orderRepository.findById(orderID).get() : null;
            if (order == null) {
                throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND + " with orderID: " + orderID);
            }
            if (order.getOrderType().equals("PARENT_ORDER")) {
                List<DesignDetail> designDetailList = detailRepository.findAllByOrderID(orderID);
                List<DesignDetail> detailList = null;
                if (!designDetailList.isEmpty()) {
                    for (DesignDetail detail : designDetailList) {
                        if (detailList == null) {
                            detailList = new ArrayList<>();
                        }
                        detailList.add(detail);
                    }
                }
                order.setDetailList(detailList);
                return orderMapper.mapToOrderCustomResponse(order);
            } else {
                List<DesignDetail> designDetailList = detailRepository.findAllBySubOrderID(orderID);
                List<DesignDetail> detailList = null;
                if (!designDetailList.isEmpty()) {
                    for (DesignDetail detail : designDetailList) {
                        if (detailList == null) {
                            detailList = new ArrayList<>();
                        }
                        detailList.add(detail);
                    }
                }
                order.setDetailList(detailList);
                return orderMapper.mapToOrderCustomResponse(order);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> getOrderById(UUID orderID) {
        return orderRepository.findAll().stream().filter(o -> o.getOrderID().equals(orderID)).findFirst();
    }

    @Override
    public List<OrderResponse> getOrderByBrandID(UUID brandID) {
        return null;
    }

    @Override
    public List<OrderResponse> getOrderByDesignID(UUID designID) {
        return null;
    }

    @Override
    public List<OrderResponse> getSubOrderByParentID(UUID parentOrderID) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getParentOrder() != null && order.getParentOrder().getOrderID().equals(parentOrderID))
                .map(this::safeMapToOrderResponse)
                .toList();
    }

    private OrderResponse safeMapToOrderResponse(Order order) {
        try {
            return orderMapper.mapToOrderResponse(order);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error mapping order to response: {}", order, e);
            // Return a default or error response
            return null;
        }
    }

    @Override
    public List<OrderResponse> getAllOrder() {
        return orderRepository.findAll().stream().map(this::safeMapToOrderResponse).toList();
    }

    @Transactional
    @Override
    public OrderResponse changeOrderStatus(OrderStatusUpdateRequest orderRequest) throws Exception {
        if (orderRequest.getOrderID() == null) {
            throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + " orderID");
        }
        var order = getOrderById(orderRequest.getOrderID());
        if (order.isEmpty()) {
            throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND + " with orderID: " + orderRequest.getOrderID());
        }

        var existedOrder = order.get();
        existedOrder.setOrderStatus(
                OrderStatus.valueOf(String.valueOf(orderRequest.getStatus()))
        );
        var updatedOrder = orderRepository.save(existedOrder);

        return orderMapper.mapToOrderResponse(updatedOrder);
    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public OrderResponse brandPickOrder(OrderPickingRequest orderPickingRequest) throws Exception {
        try {
            if (orderPickingRequest == null) {
                return null;
            }

            UUID brandID = orderPickingRequest.getBrandID();
            UUID basedOrderID = orderPickingRequest.getOrderID();
            List<UUID> detailList = orderPickingRequest.getDetailList();

            /**
             * TODO
             * check if detail is picked or not
             */

            var checkExistBrand = brandService.getBrandById(brandID);
            if (checkExistBrand.isEmpty()) {
                throw new BadRequestException(MessageConstant.CAN_NOT_FIND_BRAND + "with brandID: " + brandID);
            }
            var existedBrand = checkExistBrand.get();

            var checkBasedOrder = getOrderById(basedOrderID);
            if (checkBasedOrder.isEmpty()) {
                throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND + "with orderID: " + basedOrderID);
            }
            var basedOrder = checkBasedOrder.get();

            Design baseDesign = null;
            for (UUID detailID : detailList) {
                var checkDetail = detailRepository.getDesignDetailByDesignDetailID(detailID);
                if (checkDetail.isEmpty()) {
                    throw new BadRequestException(MessageConstant.CAN_NOT_FIND_ANY_DESIGN_DETAIL + " with detailID: " + detailID);
                }
                if (!orderRepository.getOrderByDetailID(detailID).getOrderID().equals(basedOrderID)) {
                    throw new BadRequestException("This detail " + detailID + " is inside another order!");
                }
                var detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                if (baseDesign == null) {
                    baseDesign = detail.getDesign();
                } else {
                    if (baseDesign.getDesignID() != detail.getDesign().getDesignID()) {
                        throw new BadRequestException("This detail " + detailID + " is not in the same design!");
                    }
                }
            }
            int price = baseDesign.getPartOfDesignList().stream()
                    .flatMap(partOfDesign -> Stream.concat(
                            Stream.of(partOfDesign.getMaterial()),
                            partOfDesign.getItemMaskList().stream().map(ItemMask::getMaterial)
                    ))
                    .filter(Objects::nonNull) // Lọc bỏ các vật liệu bị null
                    .collect(Collectors.toMap(
                            material -> material,
                            material -> 1,
                            Integer::sum
                    ))
                    .entrySet().stream()
                    .mapToInt(entry -> {
                        var checkBrandMaterial = brandMaterialService.getPriceByID(
                                BrandMaterialKey.builder()
                                        .brandID(brandID)
                                        .materialID(entry.getKey().getMaterialID())
                                        .build()
                        );
                        return checkBrandMaterial.map(brandMaterial -> brandMaterial.getBrandPrice() * entry.getValue()).orElse(0);
                    })
                    .sum();

            Integer quantity = 0;

            var existedBrandOrder = detailRepository.getDetailOfOrderBaseOnBrandID(basedOrderID, brandID);
            if (existedBrandOrder != null) {
                logger.info("Existed Order is updating...");
                var orderResponse = existedBrandOrder.getOrder();
                List<DesignDetail> detailResponse = new ArrayList<>();
                for (UUID detailID : detailList) {
                    var detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                    detail.setOrder(orderResponse);
                    detail.setBrand(existedBrand);
                    detail.setDetailStatus(true);
                    detailRepository.save(detail);

                    quantity += detail.getQuantity();
                    price *= detail.getQuantity();

                    detailResponse.add(detail);
                }
                orderResponse = existedBrandOrder.getOrder();
                orderResponse.setTotalPrice(price);
                orderResponse.setQuantity(quantity);
                orderRepository.save(orderResponse);

                basedOrder.setTotalPrice(basedOrder.getTotalPrice() + price);
                basedOrder.setTotalPrice(10000);
                orderRepository.save(basedOrder);

                orderResponse.setDetailList(detailResponse);
                return orderMapper.mapToOrderResponse(orderResponse);
            } else {
                logger.info("New Order is created...");
                var detail = detailRepository.getDesignDetailByDesignDetailID(detailList.get(0)).get();
                var design = detail.getDesign();
                OrderResponse createdOrder = createOrder(
                        OrderRequest
                                .builder()
                                .parentOrderID(basedOrderID)
                                .designID(design.getDesignID())
                                .orderType(basedOrder.getOrderType())
                                .quantity(0)
                                .orderStatus(OrderStatus.PENDING)
                                .address(basedOrder.getAddress())
                                .province(basedOrder.getProvince())
                                .district(basedOrder.getDistrict())
                                .ward(basedOrder.getWard())
                                .phone(basedOrder.getPhone())
                                .buyerName(basedOrder.getBuyerName())
                                .build()
                );
                var orderResponse = getOrderById(createdOrder.getOrderID()).get();
                List<DesignDetail> detailResponse = new ArrayList<>();
                for (UUID detailID : detailList) {
                    detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                    detail.setOrder(orderResponse);
                    detail.setBrand(existedBrand);
                    detail.setDetailStatus(true);
                    detailRepository.save(detail);

                    quantity += detail.getQuantity();
                    price *= detail.getQuantity();

                    detailResponse.add(detail);
                }
                orderResponse = getOrderById(createdOrder.getOrderID()).get();
                orderResponse.setTotalPrice(price);
                orderResponse.setQuantity(quantity);
                orderRepository.save(orderResponse);

                basedOrder.setTotalPrice(basedOrder.getTotalPrice() + price);
                basedOrder.setTotalPrice(10000);

                orderRepository.save(basedOrder);

                orderResponse.setDetailList(detailResponse);
                return orderMapper.mapToOrderResponse(orderResponse);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Order getOrderByDetailID(UUID detailID) {
        return orderRepository.getOrderByDetailID(detailID);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isOrderCompletelyPicked(UUID orderID) {
        try {
            logger.info("Inside method isOrderCompletelyPicked");
            var order = orderRepository.findById(orderID).get();
            var detailList = order.getDetailList();
            for (DesignDetail detail : detailList) {
                if (!detail.getDetailStatus()) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isOrderExpireTime(UUID orderID) {
        var systemPropertiesExpirationTime = systemPropertiesService.getAllByPropertyType("MATCHING_TIME");
        var order = orderRepository.findById(orderID).get();
        logger.info("Inside Method isOrderExpireTime with orderID {}", orderID);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime orderExpiredDateTime = order.getCreateDate()
//                .plusMinutes(Integer.parseInt(systemPropertiesExpirationTime.get(0).getPropertyValue()));
                .plusSeconds(30);
        logger.info("CurrentDateTime {}", currentDateTime);
        logger.info("OrderExpiredDateTime {}", orderExpiredDateTime);
        return currentDateTime.isAfter(orderExpiredDateTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllParentOrder() {
        return orderRepository
                .findAll()
                .stream()
                .filter(orderResponse -> orderResponse.getOrderType().equals("PARENT_ORDER"))
                .map(this::safeMapToOrderResponse)
                .toList();
    }

    @Override
    public List<String> filterBrandForSpecificOrderBaseOnDesign(UUID designID) {
        logger.info("DesignID {}", designID);
        var designResponse = designService.getDesignByID(designID);
        if (designResponse == null) {
            throw new ItemNotFoundException("Can not find Design By Design ID: " + designID);
        }

        // Find all brands by Expert Tailoring ID of the Design
        var expertTailoring = designResponse.getExpertTailoring();
        var brandExpertTailoringSelected = brandService.findAllBrandByExpertTailoringID(expertTailoring.getExpertTailoringID());

        brandExpertTailoringSelected.forEach(brand -> logger.info("Brand Selected by Expert Tailoring: {}", brand.getUser().getEmail()));


        // Get all material IDs of the design
        Set<UUID> designMaterialIDs = new HashSet<>();
        designResponse.getPartOfDesignList().forEach(partOfDesign -> {
            var materialPartOfDesign = partOfDesign.getMaterial();
            if (materialPartOfDesign != null) {
                designMaterialIDs.add(materialPartOfDesign.getMaterialID());
            }
            partOfDesign.getItemMaskList().forEach(itemMask -> {
                var materialItemMask = itemMask.getMaterial();
                if (materialItemMask != null) {
                    designMaterialIDs.add(materialItemMask.getMaterialID());
                }
            });
        });

        designMaterialIDs.forEach(designMaterial -> logger.info("Design Material ID {}", designMaterial));

        // Filter brands that have all materials used in the design
        List<String> brandResponses = new ArrayList<>();
        for (var brand : brandExpertTailoringSelected) {
            var brandMaterials = brandMaterialService.getAllBrandMaterialByBrandID(brand.getBrandID());
            long matchingMaterialCount = designMaterialIDs
                    .stream()
                    .filter(designMaterialID ->
                            brandMaterials.stream().anyMatch(brandMaterial -> brandMaterial.getMaterialID().toString().equals(designMaterialID.toString())))
                    .count();
            if (matchingMaterialCount == designMaterialIDs.size()) {
                brandResponses.add(brand.getUser().getEmail());
            }
        }
        return brandResponses;
    }
}
