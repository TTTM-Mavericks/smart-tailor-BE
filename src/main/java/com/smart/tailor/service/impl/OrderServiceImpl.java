package com.smart.tailor.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.*;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.enums.PaymentType;
import com.smart.tailor.event.CreateOrderEvent;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.exception.ItemNotFoundException;
import com.smart.tailor.mapper.DesignDetailMapper;
import com.smart.tailor.mapper.OrderMapper;
import com.smart.tailor.mapper.PaymentMapper;
import com.smart.tailor.repository.DesignDetailRepository;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.*;
import com.smart.tailor.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BrandService brandService;
    private final DesignService designService;
    private final CustomerService customerService;
    private final UserService userService;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;
    private final DesignDetailMapper detailMapper;
    private final BrandMaterialService brandMaterialService;
    private final DesignDetailRepository detailRepository;
    private final PaymentService paymentService;
    private final SystemPropertiesService systemPropertiesService;
    private final BrandPropertiesService brandPropertiesService;
    private final EmployeeService employeeService;
    private final OrderStageService stageService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final GHTKShippingService ghtkShippingService;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Value("${client.server.link}")
    private String clientServerLink;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) throws Exception {
        try {
            if (!Utilities.isStringNotNullOrEmpty(orderRequest.getDesignID().toString())) {
                throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designID");
            }
            String designID = orderRequest.getDesignID();

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
                            .expectedStartDate(LocalDateTime.now().plusDays(1))
                            .employee(getSuitableEmp())
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
                    .totalPrice(0)
                    .orderType("PARENT_ORDER")
                    .expectedStartDate(LocalDateTime.now().plusDays(1))
                    .build();
            var orderResponse = orderRepository.save(order);
            return orderMapper.mapToOrderResponse(orderResponse);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<OrderResponse> getParentOrderByDesignID(String designID) throws Exception {
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
    public void updateOrderStatus(String orderID, String orderStatus) {
        var order = getOrderById(orderID).isPresent() ? getOrderById(orderID).get() : null;
        if (order == null) {
            throw new BadRequestException(MessageConstant.RESOURCE_NOT_FOUND);
        }
        order.setOrderStatus(OrderStatus.valueOf(orderStatus));
        updateOrder(order);
    }

    private OrderCustomResponse convertToOrderCustomResponse(Order order, List<DesignDetail> designDetails) {
        return OrderCustomResponse
                .builder()
                .designResponse(designService.getDesignByOrderID(order.getOrderID()))
                .parentOrderID(order.getParentOrder() != null ? order.getParentOrder().getOrderID() : null)
                .orderType(order.getOrderType())
                .orderID(order.getOrderID())
                .quantity(order.getQuantity())
                .orderStatus(order.getOrderStatus())
                .rating(order.getRating())
                .address(order.getAddress())
                .province(order.getProvince())
                .labelID(order.getLabelID())
                .district(order.getDistrict())
                .ward(order.getWard())
                .phone(order.getPhone())
                .buyerName(order.getBuyerName())
                .totalPrice(order.getTotalPrice())
                .expectedStartDate(Utilities.convertLocalDateTimeToString(order.getExpectedStartDate()))
                .expectedProductCompletionDate(Utilities.convertLocalDateTimeToString(order.getExpectedProductCompletionDate()))
                .estimatedDeliveryDate(Utilities.convertLocalDateTimeToString(order.getEstimatedDeliveryDate()))
                .productionStartDate(Utilities.convertLocalDateTimeToString(order.getProductionStartDate()))
                .productionCompletionDate(Utilities.convertLocalDateTimeToString(order.getProductionCompletionDate()))
                .createDate(order.getCreateDate() != null ? order.getCreateDate().toString() : null)
                .detailList(
                        designDetails
                                .stream()
                                .map(detailMapper::mapperToDesignDetailResponse)
                                .toList()
                )
                .paymentList(
                        paymentService.findAllByOrderID(order.getOrderID())
                                .stream()
                                .map(paymentMapper::mapperToPaymentResponse)
                                .toList()
                )
                .build();
    }

    @Override
    public OrderCustomResponse getOrderByOrderID(String orderID) throws Exception {
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
//                order.setDetailList(detailList);
                var status = order.getOrderStatus();
                var paymentList = paymentService.findAllByOrderID(orderID);
                switch (status) {
                    case DEPOSIT -> {
                        logger.error("INCASE DEPOSIT");
                        if (!paymentList.isEmpty()) {
                            var checkDeposited = paymentList.stream().filter(p ->
                                    p.getPaymentType().equals(PaymentType.DEPOSIT) &&
                                            p.getPaymentStatus()
                            ).findFirst();

                            // check if deposited?
                            if (checkDeposited.isPresent()) {
                                // change status of parent order to PROCESSING
                                logger.info("Change Status PROCESSING Order");
                                changeOrderStatus(
                                        OrderStatusUpdateRequest
                                                .builder()
                                                .orderID(orderID.toString())
                                                .status(OrderStatus.PROCESSING.name())
                                                .build()
                                );

                                // change status of sub order to START_PRODUCING
                                var subOrderList = getSubOrderByParentID(orderID);
                                for (OrderResponse subOrder : subOrderList) {
                                    var subOrderObject = getOrderById(subOrder.getOrderID()).get();
                                    subOrderObject.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
                                    updateOrder(subOrderObject);
                                }
                            }
                        }
                    }
                    case PROCESSING -> {
                        logger.error("INCASE PROCESSING");

                        int divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
                        if (order.getQuantity() >= divideNumber) {

                            // CHECK CURRENT STAGE
                            var stage = -1;
                            if (!paymentList.isEmpty()) {
                                var checkDeposited = paymentList.stream().filter(p ->
                                        p.getPaymentType().equals(PaymentType.STAGE_2)
                                ).findFirst();
                                if (checkDeposited.isEmpty()) {
                                    checkDeposited = paymentList.stream().filter(p ->
                                            p.getPaymentType().equals(PaymentType.STAGE_1)
                                    ).findFirst();
                                    if (checkDeposited.isPresent()) {
                                        if (checkDeposited.get().getPaymentStatus()) {
                                            stage = 1;
                                        } else {
                                            stage = 0;
                                        }
                                    } else {
                                        stage = 0;
                                    }
                                } else {
                                    if (checkDeposited.get().getPaymentStatus())
                                        stage = 2;
                                }
                            }

                            var subOrderList = getSubOrderByParentID(orderID);
                            boolean isFinish = true;
                            switch (stage) {
                                case 0:
                                    isFinish = true;
                                    for (OrderResponse subOrder : subOrderList) {
                                        if (!subOrder.getOrderStatus().equals(OrderStatus.FINISH_FIRST_STAGE)
                                                && !subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                            isFinish = false;
                                        }
                                    }
                                    if (isFinish) {
                                        var checkDeposited = paymentList.stream().filter(p ->
                                                p.getPaymentType().equals(PaymentType.STAGE_1)
                                        ).findFirst();
                                        if (checkDeposited.isEmpty()) {
                                            logger.error("CREATE STAGE_1");
                                            var payOSResponse = paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(orderID)

                                                            .paymentSenderID(null)
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(null)
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.STAGE_1)
                                                            .paymentAmount(order.getTotalPrice())
                                                            .itemList(null)
                                                            .build()
                                            );
                                            for (OrderResponse subOrderResponse : subOrderList) {
                                                var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                                                subOrder.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
                                                updateOrder(subOrder);
                                            }
                                        }
                                    }
                                    break;
                                case 1:
                                    isFinish = true;
                                    for (OrderResponse subOrder : subOrderList) {
                                        if (!subOrder.getOrderStatus().equals(OrderStatus.FINISH_SECOND_STAGE)
                                                && !subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                            isFinish = false;
                                        }
                                    }
                                    if (isFinish) {
                                        var checkDeposited = paymentList.stream().filter(p ->
                                                p.getPaymentType().equals(PaymentType.STAGE_2)
                                        ).findFirst();
                                        if (checkDeposited.isEmpty()) {
                                            logger.error("CREATE STAGE_2");
                                            var payOSResponse = paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(orderID)

                                                            .paymentSenderID(null)
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(null)
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.STAGE_2)
                                                            .paymentAmount(order.getTotalPrice())
                                                            .itemList(null)
                                                            .build()
                                            );
                                            for (OrderResponse subOrderResponse : subOrderList) {
                                                var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                                                subOrder.setOrderStatus(OrderStatus.CHECKING_SAMPLE_DATA);
                                                updateOrder(subOrder);
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    isFinish = true;
                                    for (OrderResponse subOrder : subOrderList) {
                                        if (!subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                            isFinish = false;
                                        }
                                    }
                                    if (isFinish) {
                                        var maxDateTime = LocalDateTime.parse(subOrderList.get(0).getProductionCompletionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                        for (OrderResponse subOrder : subOrderList) {
                                            var completionDate = LocalDateTime.parse(subOrder.getProductionCompletionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                            maxDateTime = maxDateTime.isAfter(completionDate) ? maxDateTime : completionDate;
                                        }
                                        order.setProductionCompletionDate(maxDateTime);
                                        updateOrder(order);
                                        changeOrderStatus(
                                                OrderStatusUpdateRequest
                                                        .builder()
                                                        .orderID(String.valueOf(orderID))
                                                        .status(OrderStatus.COMPLETED.name())
                                                        .build()
                                        );
                                        break;
                                    }
                            }
                        } else {
                            var subOrderList = getSubOrderByParentID(orderID);
                            boolean isFinish = true;
                            isFinish = true;
                            for (OrderResponse subOrder : subOrderList) {
                                if (!subOrder.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                    isFinish = false;
                                }
                            }
                            if (isFinish) {
                                var checkDeposited = paymentList.stream().filter(p ->
                                        p.getPaymentType().equals(PaymentType.COMPLETED_ORDER)
                                ).findFirst();
                                if (checkDeposited.isEmpty()) {
                                    logger.error("CREATE COMPLETE_ORDER");
                                    var payOSResponse = paymentService.createPayOSPayment(
                                            PaymentRequest
                                                    .builder()
                                                    .orderID(orderID)

                                                    .paymentSenderID(null)
                                                    .paymentSenderName(order.getBuyerName())
                                                    .paymentSenderBankCode("")
                                                    .paymentSenderBankNumber("")

                                                    .paymentRecipientID(null)
                                                    .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                    .paymentRecipientBankCode("OCB")
                                                    .paymentRecipientBankNumber("0163100007285002")

                                                    .paymentType(PaymentType.COMPLETED_ORDER)
                                                    .paymentAmount(order.getTotalPrice())
                                                    .itemList(null)
                                                    .build()
                                    );
                                }
                                logger.info("Change Status PROCESSING Order");
                                changeOrderStatus(
                                        OrderStatusUpdateRequest
                                                .builder()
                                                .orderID(orderID.toString())
                                                .status(OrderStatus.COMPLETED.name())
                                                .build()
                                );
                            }
                        }
                    }
                    case DELIVERED -> {
                        var subOrderList = getSubOrderByParentID(orderID);
                        for (var subOrderResponse : subOrderList) {
                            var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                            paymentService.createManualPayment(
//                                    PaymentRequest
//                                            .builder()
//                                            .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                            .paymentSenderName("NGUYEN VAN A")
//                                            .paymentSenderBankCode("NCB")
//                                            .paymentSenderBankNumber("9704198526191432198")
//                                            .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                            .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                            .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                            .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                            .orderID(subOrder.getOrderID())
//                                            .paymentAmount(subOrder.getTotalPrice())
//                                            .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                            .paymentType(PaymentType.BRAND_INVOICE)
//                                            .build()
//                            );
                            if(order.getOrderStatus().name().equals(OrderStatus.DELIVERED.name()) &&
                                    Optional.ofNullable(order.getLabelID()).isEmpty() && order.getOrderType().equals("PARENT_ORDER")) {
                                var design = designService.getDesignByOrderID(subOrder.getOrderID());
                                logger.info("Design Information: {}", design);

                                var minWeightParentOrder = design.getMinWeight() * order.getQuantity();
                                logger.info("Minimum Weight for Parent Order (Quantity {}): {}", order.getQuantity(), minWeightParentOrder);

                                var maxWeightParentOrder = design.getMaxWeight() * order.getQuantity();
                                logger.info("Maximum Weight for Parent Order (Quantity {}): {}", order.getQuantity(), maxWeightParentOrder);

                                var averageWeightParentOrder = (float) (minWeightParentOrder + maxWeightParentOrder) / 2;
                                logger.info("Average Weight for Parent Order: {}", averageWeightParentOrder);

                                var maximumShippingWeight = Integer.parseInt(systemPropertiesService.getByName("MAX_SHIPPING_WEIGHT").getPropertyValue());
                                if(averageWeightParentOrder < maximumShippingWeight){
                                    OrderShippingRequest.OrderShippingDetailRequest orderShippingDetailRequest =
                                            new OrderShippingRequest.OrderShippingDetailRequest(
                                                    order.getOrderID() + " " + LocalDateTime.now(),
                                                    "Smart Tailor Services",
                                                    "344 Lê Văn Việt",
                                                    "Hồ Chí Minh",
                                                    "Thủ Đức",
                                                    "Tăng Nhơn Phú B",
                                                    "0926733445",
                                                    order.getPhone(),
                                                    order.getBuyerName(),
                                                    order.getAddress(),
                                                    order.getProvince(),
                                                    order.getDistrict(),
                                                    order.getWard(),
                                                    "Khác",
                                                    "1",
                                                    "2024/10/08",
                                                    0,
                                                    averageWeightParentOrder,
                                                    1
                                            );

                                    var orderShippingRequest =
                                            OrderShippingRequest
                                                    .builder()
                                                    .order(orderShippingDetailRequest)
                                                    .build();

                                    var createShippingOrder = ghtkShippingService.createShippingOrder(orderShippingRequest);
                                    if(createShippingOrder != null){
                                        logger.info("Create Shipping Order Successfully {}", createShippingOrder);
                                    }
                                }
                            }
                            if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                var payOSResponse = paymentService.createPayOSPayment(
                                        PaymentRequest
                                                .builder()
                                                .orderID(subOrder.getOrderID())

                                                .paymentSenderID(null)
                                                .paymentSenderName(order.getBuyerName())
                                                .paymentSenderBankCode("")
                                                .paymentSenderBankNumber("")

                                                .paymentRecipientID(null)
                                                .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                .paymentRecipientBankCode("OCB")
                                                .paymentRecipientBankNumber("0163100007285002")

                                                .paymentType(PaymentType.BRAND_INVOICE)
                                                .paymentAmount(subOrder.getTotalPrice())
                                                .itemList(null)
                                                .build()
                                );
                            }
                        }
                    }
                    case CANCEL -> {
                        // CHECK CURRENT STAGE
                        var stage = -1;
                        if (!paymentList.isEmpty()) {
                            var checkDeposited = paymentList.stream().filter(p ->
                                    p.getPaymentType().equals(PaymentType.STAGE_2)
                            ).findFirst();
                            if (checkDeposited.isEmpty()) {
                                checkDeposited = paymentList.stream().filter(p ->
                                        p.getPaymentType().equals(PaymentType.STAGE_1)
                                ).findFirst();
                                if (checkDeposited.isPresent()) {
                                    if (checkDeposited.get().getPaymentStatus()) {
                                        stage = 1;
                                    } else {
                                        stage = 0;
                                    }
                                } else {
                                    stage = 0;
                                }
                            } else {
                                if (checkDeposited.get().getPaymentStatus())
                                    stage = 2;
                            }
                        }
                        var subOrderList = getSubOrderByParentID(orderID);
                        boolean isFinish = true;
                        switch (stage) {
                            case 0 -> {
                                isFinish = true;
                                for (OrderResponse subOrderResponse : subOrderList) {
                                    if (subOrderResponse.getOrderStatus().equals(OrderStatus.FINISH_FIRST_STAGE)) {
                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                        paymentService.createManualPayment(
//                                                PaymentRequest
//                                                        .builder()
//                                                        .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                                        .paymentSenderName("NGUYEN VAN A")
//                                                        .paymentSenderBankCode("NCB")
//                                                        .paymentSenderBankNumber("9704198526191432198")
//                                                        .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                                        .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                                        .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                                        .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                                        .orderID(subOrder.getOrderID())
//                                                        .paymentAmount(subOrder.getTotalPrice())
//                                                        .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                                        .paymentType(PaymentType.BRAND_INVOICE)
//                                                        .build()
//                                        );
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            var payOSResponse = paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(subOrder.getOrderID())

                                                            .paymentSenderID(null)
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(null)
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.BRAND_INVOICE)
                                                            .paymentAmount(subOrder.getTotalPrice())
                                                            .itemList(null)
                                                            .build()
                                            );
                                        }
                                        changeOrderStatus(
                                                OrderStatusUpdateRequest
                                                        .builder()
                                                        .orderID(String.valueOf(subOrder.getOrderID()))
                                                        .status(OrderStatus.CANCEL.name())
                                                        .build()
                                        );
                                    }
                                }
                            }
                            case 1 -> {
                                isFinish = true;
                                for (OrderResponse subOrderResponse : subOrderList) {
                                    if (subOrderResponse.getOrderStatus().equals(OrderStatus.FINISH_SECOND_STAGE)) {
                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                        paymentService.createManualPayment(
//                                                PaymentRequest
//                                                        .builder()
//                                                        .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                                        .paymentSenderName("NGUYEN VAN A")
//                                                        .paymentSenderBankCode("NCB")
//                                                        .paymentSenderBankNumber("9704198526191432198")
//                                                        .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                                        .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                                        .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                                        .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                                        .orderID(subOrder.getOrderID())
//                                                        .paymentAmount(subOrder.getTotalPrice())
//                                                        .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                                        .paymentType(PaymentType.BRAND_INVOICE)
//                                                        .build()
//                                        );
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            var payOSResponse = paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(subOrder.getOrderID())

                                                            .paymentSenderID(null)
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(null)
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.BRAND_INVOICE)
                                                            .paymentAmount(subOrder.getTotalPrice())
                                                            .itemList(null)
                                                            .build()
                                            );
                                        }
                                        changeOrderStatus(
                                                OrderStatusUpdateRequest
                                                        .builder()
                                                        .orderID(String.valueOf(subOrder.getOrderID()))
                                                        .status(OrderStatus.CANCEL.name())
                                                        .build()
                                        );
                                    }
                                }
                            }
                            case 2 -> {
                                isFinish = true;
                                for (OrderResponse subOrderResponse : subOrderList) {
                                    if (subOrderResponse.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                                        var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
//                                        paymentService.createManualPayment(
//                                                PaymentRequest
//                                                        .builder()
//                                                        .paymentSenderID(userService.getUserByEmail("accountantsmarttailor123@gmail.com").getUserID())
//                                                        .paymentSenderName("NGUYEN VAN A")
//                                                        .paymentSenderBankCode("NCB")
//                                                        .paymentSenderBankNumber("9704198526191432198")
//                                                        .paymentRecipientID(subOrder.getDetailList().get(0).getBrand().getBrandID())
//                                                        .paymentRecipientName(subOrder.getDetailList().get(0).getBrand().getBrandName())
//                                                        .paymentRecipientBankCode(subOrder.getDetailList().get(0).getBrand().getBankName())
//                                                        .paymentRecipientBankNumber(subOrder.getDetailList().get(0).getBrand().getAccountNumber())
//                                                        .orderID(subOrder.getOrderID())
//                                                        .paymentAmount(subOrder.getTotalPrice())
//                                                        .paymentMethod(PaymentMethod.CREDIT_CARD)
//                                                        .paymentType(PaymentType.BRAND_INVOICE)
//                                                        .build()
//                                        );
                                        if (subOrder.getPaymentList() == null || subOrder.getPaymentList().isEmpty()) {
                                            var payOSResponse = paymentService.createPayOSPayment(
                                                    PaymentRequest
                                                            .builder()
                                                            .orderID(subOrder.getOrderID())

                                                            .paymentSenderID(null)
                                                            .paymentSenderName(order.getBuyerName())
                                                            .paymentSenderBankCode("")
                                                            .paymentSenderBankNumber("")

                                                            .paymentRecipientID(null)
                                                            .paymentRecipientName("NGUYEN HOANG LAM TRUONG")
                                                            .paymentRecipientBankCode("OCB")
                                                            .paymentRecipientBankNumber("0163100007285002")

                                                            .paymentType(PaymentType.BRAND_INVOICE)
                                                            .paymentAmount(subOrder.getTotalPrice())
                                                            .itemList(null)
                                                            .build()
                                            );
                                        }
                                        changeOrderStatus(
                                                OrderStatusUpdateRequest
                                                        .builder()
                                                        .orderID(String.valueOf(subOrder.getOrderID()))
                                                        .status(OrderStatus.CANCEL.name())
                                                        .build()
                                        );
                                    }
                                }
                            }
                        }
                    }
                }
                return convertToOrderCustomResponse(order, detailList);
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
//                order.setDetailList(detailList);
                return convertToOrderCustomResponse(order, detailList);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public OrderCustomResponse getOrderDetailByOrderID(String orderID) throws Exception {
        try {
            var response = getOrderByOrderID(orderID);

            var paymentNewest = response
                    .getPaymentList()
                    .stream()
                    .max(Comparator.comparing(PaymentResponse::getCreateDate));

            List<PaymentResponse> paymentList = new ArrayList<>();
            if (paymentNewest.isPresent() && !paymentNewest.get().getPaymentStatus())
                paymentList.add(paymentNewest.get());

            response.setPaymentList(paymentList);
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Order> getOrderById(String orderID) {
        return orderRepository.findById(orderID);
    }

    @Override
    public List<OrderCustomResponse> getOrderByBrandID(String brandID) throws Exception {
        var listOrder = orderRepository.findOrderByBrandID(brandID);
        List<OrderCustomResponse> responseList = new ArrayList<>();
        for (Order o : listOrder) {
            responseList.add(getOrderByOrderID(o.getOrderID()));
        }
        return responseList;
    }

    @Override
    public List<OrderResponse> getOrderByDesignID(String designID) {
        return orderRepository.findParentOrderByDesignID(designID).stream().map(this::safeMapToOrderResponse).toList();
    }

    @Override
    public List<OrderCustomResponse> getOrderByUserID(String userID) throws Exception {
        var orderList = orderRepository.findParentOrderByUserID(userID);
        List<OrderCustomResponse> responseList = new ArrayList<>();
        for (Order o : orderList) {
            responseList.add(getOrderByOrderID(o.getOrderID()));
        }
        return responseList;
    }

    @Override
    public List<OrderResponse> getSubOrderByParentID(String parentOrderID) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getParentOrder() != null
                        && order.getParentOrder().getOrderID().equals(parentOrderID)
                        && !order.getOrderStatus().equals(OrderStatus.CANCEL)
                )
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

    @Override
    public OrderResponse changeOrderStatus(OrderStatusUpdateRequest orderRequest) throws Exception {
        String orderID = orderRequest.getOrderID();
        var order = getOrderById(orderID);
        if (order.isEmpty()) {
            throw new ItemNotFoundException("Can not find Order with OrderID: " + orderID);
        }

        var existedOrder = order.get();
        existedOrder.setOrderStatus(OrderStatus.valueOf(orderRequest.getStatus()));

        if (existedOrder.getOrderStatus().equals(OrderStatus.CANCEL)) {
            if (existedOrder.getOrderType().equals("PARENT_ORDER")) {
                var subOrderList = getSubOrderByParentID(existedOrder.getOrderID());
                for (var subOrderResponse : subOrderList) {
                    var subOrder = getOrderById(subOrderResponse.getOrderID()).get();
                    subOrder.setOrderStatus(OrderStatus.CANCEL);
                    orderRepository.save(subOrder);
                }
            } else {
//<<<<<<< HEAD
//                if (orderRequest.getStatus().equals(OrderStatus.FINISH_FIRST_STAGE.name())
//                        || orderRequest.getStatus().equals(OrderStatus.FINISH_SECOND_STAGE.name())) {
//                    var stage = stageService.getOrderStageByID(
//                           (
//                                    stageService.getOrderStageByOrderID(
//                                                    existedOrder.getParentOrder().getOrderID()
//                                            )
//                                            .stream()
//                                            .filter(
//                                                    s -> s.getStage().equals(OrderStatus.PROCESSING)
//                                            )
//                                            .findFirst()
//                                            .get().getStageId()
//                            )
//                    );
//                    var subStage = stageService.getOrderStageByID(
//                            (
//                                    stageService.getOrderStageByOrderID(existedOrder.getOrderID())
//                                            .stream()
//                                            .filter(s -> s.getStage().equals(existedOrder.getOrderStatus()))
//                                            .findFirst()
//                                            .get().getStageId()
//                            )
//                    );
//                    if (stage != null) {
//                        stage.setCurrentQuantity(
//                                stage.getCurrentQuantity() + subStage.getCurrentQuantity()
//=======
                var parentOrder = getOrderById(existedOrder.getParentOrder().getOrderID()).get();
                parentOrder.setOrderStatus(OrderStatus.PENDING);
                orderRepository.save(parentOrder);

                var detailList = existedOrder.getDetailList();
                for (var detail : detailList) {
                    detail.setOrder(parentOrder);
                    detail.setBrand(null);
                    detail.setDetailStatus(false);
                    detailRepository.save(detail);
                }
                existedOrder.setDetailList(null);
                var orderResponse = safeMapToOrderResponse(parentOrder);
                applicationEventPublisher.publishEvent(new CreateOrderEvent(orderResponse));
            }
        } else {
            if (orderRequest.getStatus().equals(OrderStatus.START_PRODUCING.name())) {
                existedOrder.setProductionStartDate(LocalDateTime.now());
            } else {
                if (orderRequest.getStatus().equals(OrderStatus.COMPLETED.name())) {
                    existedOrder.setProductionCompletionDate(LocalDateTime.now());
                } else {
                    if (orderRequest.getStatus().equals(OrderStatus.FINISH_FIRST_STAGE.name())
                            || orderRequest.getStatus().equals(OrderStatus.FINISH_SECOND_STAGE.name())) {
                        var stage = stageService.getOrderStageByID(
                                (
                                        stageService.getOrderStageByOrderID(
                                                        existedOrder.getParentOrder().getOrderID()
                                                )
                                                .stream()
                                                .filter(
                                                        s -> s.getStage().equals(OrderStatus.PROCESSING)
                                                )
                                                .findFirst()
                                                .get().getStageId()
                                )
//>>>>>>> feature
                        );
                        var subStage = stageService.getOrderStageByID(
                                (
                                        stageService.getOrderStageByOrderID(existedOrder.getOrderID())
                                                .stream()
                                                .filter(s -> s.getStage().equals(existedOrder.getOrderStatus()))
                                                .findFirst()
                                                .get().getStageId()
                                )
                        );
                        if (stage != null) {
                            stage.setCurrentQuantity(
                                    stage.getCurrentQuantity() + subStage.getCurrentQuantity()
                            );
                            stageService.updateStage(stage);
                        }
                    }
                }
            }
        }
        var stageResponseList = stageService.getOrderStageByOrderID(orderID);
        for (var stageResponse : stageResponseList) {
            if (stageResponse.getStage().equals(OrderStatus.valueOf(orderRequest.getStatus()))) {
                var stage = stageService.getOrderStageByID(stageResponse.getStageId());
                stage.setStatus(true);
                stageService.updateStage(stage);
            }
        }
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

            String brandID = orderPickingRequest.getBrandID();
            String basedOrderID = orderPickingRequest.getOrderID();
            List<String> detailList = orderPickingRequest.getDetailList();

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
            for (String detailID : detailList) {
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
                for (String detailID : detailList) {
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
                var wageProperty = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
                var dayCompleted = Integer.parseInt(
                        brandPropertiesService.getByBrandIDAndPropertyID(
                                brandID,
                                wageProperty.getPropertyID()
                        ).getBrandPropertyValue()
                );
                var oldCompleteDate = orderResponse.getExpectedProductCompletionDate();
                var newCompleteDate = orderResponse.getExpectedStartDate().plusDays((long) Math.ceil(quantity / dayCompleted));
                orderResponse.setExpectedProductCompletionDate(
                        oldCompleteDate.isAfter(newCompleteDate) ? oldCompleteDate : newCompleteDate
                );
                updateOrder(orderResponse);
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
                for (String detailID : detailList) {
                    detail = detailRepository.getDesignDetailByDesignDetailID(detailID).get();
                    detail.setOrder(orderResponse);
                    detail.setBrand(existedBrand);
                    detail.setDetailStatus(true);
                    detailRepository.save(detail);

                    quantity += detail.getQuantity();
                    price *= detail.getQuantity();

                    detailResponse.add(detail);
                }

                var wageProperty = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
                var dayCompleted = Integer.parseInt(
                        brandPropertiesService.getByBrandIDAndPropertyID(
                                brandID,
                                wageProperty.getPropertyID()
                        ).getBrandPropertyValue()
                );
                logger.info("Day Completed Line 641 {}", dayCompleted);
                logger.info("Order Quantity Line 642 {}", quantity);
                orderResponse.setExpectedProductCompletionDate(
                        orderResponse.getExpectedStartDate()
                                .plusDays((long) Math.ceil(quantity / dayCompleted))
                );
                updateOrder(orderResponse);

                orderResponse = getOrderById(createdOrder.getOrderID()).get();
                orderResponse.setTotalPrice(price);
                orderResponse.setQuantity(quantity);
                orderRepository.save(orderResponse);

                basedOrder.setTotalPrice(basedOrder.getTotalPrice() + price);
                basedOrder.setTotalPrice(10000);

                orderRepository.save(basedOrder);

                int divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
                stageService.createOrderStage(
                        OrderStageRequest
                                .builder()
                                .orderID(createdOrder.getOrderID())
                                .stage(OrderStatus.START_PRODUCING)
                                .currentQuantity(0)
                                .status(false)
                                .build()
                );

                if (quantity >= divideNumber) {
                    int eachPhase = Utilities.roundToNearestHalf(quantity * 1.0 / 3);
                    stageService.createOrderStage(
                            OrderStageRequest
                                    .builder()
                                    .orderID(createdOrder.getOrderID())
                                    .stage(OrderStatus.FINISH_FIRST_STAGE)
                                    .currentQuantity(eachPhase)
                                    .status(false)
                                    .build()
                    );
                    stageService.createOrderStage(
                            OrderStageRequest
                                    .builder()
                                    .orderID(createdOrder.getOrderID())
                                    .stage(OrderStatus.FINISH_SECOND_STAGE)
                                    .currentQuantity(eachPhase)
                                    .status(false)
                                    .build()
                    );
                    stageService.createOrderStage(
                            OrderStageRequest
                                    .builder()
                                    .orderID(createdOrder.getOrderID())
                                    .stage(OrderStatus.COMPLETED)
                                    .currentQuantity(orderResponse.getQuantity() - (eachPhase * 2))
                                    .status(false)
                                    .build()
                    );
                } else {
                    stageService.createOrderStage(
                            OrderStageRequest
                                    .builder()
                                    .orderID(createdOrder.getOrderID())
                                    .stage(OrderStatus.COMPLETED)
                                    .currentQuantity(orderResponse.getQuantity())
                                    .status(false)
                                    .build()
                    );
                }

                orderResponse.setDetailList(detailResponse);
                return orderMapper.mapToOrderResponse(orderResponse);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Order getOrderByDetailID(String detailID) {
        return orderRepository.getOrderByDetailID(detailID);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isOrderCompletelyPicked(String orderID) {
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
    public Boolean isOrderExpireTime(String orderID) {
        var systemPropertiesExpirationTime = systemPropertiesService.getByName("MATCHING_TIME");
        var order = orderRepository.findById(orderID).get();
        logger.info("Inside Method isOrderExpireTime with orderID {}", orderID);
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime orderExpiredDateTime = order.getCreateDate()
                .plusMinutes(Integer.parseInt(systemPropertiesExpirationTime.getPropertyValue()));
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
    public List<String> filterBrandForSpecificOrderBaseOnDesign(String designID) {
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
        Set<String> designMaterialIDs = new HashSet<>();
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

    @Override
    public void confirmOrder(String orderID) {
        try {
            var checkOrder = getOrderById(orderID);
            if (checkOrder.isEmpty()) {
                throw new Exception(MessageConstant.RESOURCE_NOT_FOUND);
            }
            var order = checkOrder.get();

            var subOrderList = getSubOrderByParentID(orderID);
            String maxDate = subOrderList.get(0).getExpectedProductCompletionDate();
            var convertMaxDate = LocalDateTime.parse(maxDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//            LocalDateTime maxDate = subOrderList.get(0).getExpectedProductCompletionDate();
            int quantity = 0;
            for (var subOrder : subOrderList) {
                var expectedCompleteDate = subOrder.getExpectedProductCompletionDate();
                var convertExpectedCompleteDate = LocalDateTime.parse(expectedCompleteDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                maxDate = convertExpectedCompleteDate.isAfter(convertMaxDate) ? expectedCompleteDate : maxDate;
                quantity += subOrder.getQuantity();
            }
            order.setExpectedProductCompletionDate(LocalDateTime.parse(maxDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            updateOrder(order);

            int divideNumber = Integer.parseInt(systemPropertiesService.getByName("DIVIDE_NUMBER").getPropertyValue());
            stageService.createOrderStage(
                    OrderStageRequest
                            .builder()
                            .orderID(orderID)
                            .stage(OrderStatus.DEPOSIT)
                            .currentQuantity(0)
                            .status(false)
                            .build()
            );

            stageService.createOrderStage(
                    OrderStageRequest
                            .builder()
                            .orderID(orderID)
                            .stage(OrderStatus.PROCESSING)
                            .currentQuantity(0)
                            .status(false)
                            .build()
            );

            stageService.createOrderStage(
                    OrderStageRequest
                            .builder()
                            .orderID(orderID)
                            .stage(OrderStatus.COMPLETED)
                            .currentQuantity(quantity)
                            .status(false)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderStageResponse> getOrderStageByOrderID(String orderID) {
        return stageService.getOrderStageByOrderID(orderID);
    }

    private Employee getSuitableEmp() {
        try {
            var empList = employeeService.getAll();
            if (empList.isEmpty()) {
                return null;
            }
            empList.sort(
                    Comparator.comparingInt(Employee::getPendingTask)
                            .thenComparingInt(Employee::getTotalTask)
                            .thenComparing(Employee::getFailTask, Comparator.reverseOrder())
            );
            return empList.get(0);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Transactional
    @Override
    public void ratingOrder(RatingOrderRequest ratingOrderRequest) {
        var user = userService.getUserByUserID(ratingOrderRequest.getUserID())
                .orElseThrow(() -> new ItemNotFoundException("Cannot find User with UserID: " + ratingOrderRequest.getUserID()));

        var parentOrder = orderRepository.findById(ratingOrderRequest.getParentOrderID())
                .orElseThrow(() -> new ItemNotFoundException("Cannot find Order with OrderID: " + ratingOrderRequest.getParentOrderID()));

        var orderRating = ratingOrderRequest.getRating();
        parentOrder.setRating(orderRating);

        // Save Rating for Order
        orderRepository.save(parentOrder);

        // Rating Brand Contribute to Order
        var subOrderList = getSubOrderByParentID(parentOrder.getOrderID());
        var estimateOrderTimeLine = getOrderTimeLineByParentOrderID(parentOrder.getOrderID());

        // Define the format for parsing and formatting dates
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Convert estimated dates to LocalDateTime
        LocalDateTime estimatedDateFinishFirstStage = LocalDateTime.parse(estimateOrderTimeLine.getEstimatedDateFinishFirstStage(), outputFormatter);
        LocalDateTime estimatedDateFinishSecondStage = LocalDateTime.parse(estimateOrderTimeLine.getEstimatedDateFinishSecondStage(), outputFormatter);
        LocalDateTime estimatedDateCompletion = LocalDateTime.parse(estimateOrderTimeLine.getEstimatedDateFinishCompleteStage(), outputFormatter);

        logger.info("Estimated Date Finish First Stage: {}", estimatedDateFinishFirstStage);
        logger.info("Estimated Date Finish Second Stage: {}", estimatedDateFinishSecondStage);
        logger.info("Estimated Date Completion: {}", estimatedDateCompletion);

        for (var subOrder : subOrderList) {
            var designDetail = detailRepository.getDesignDetailBySubOrderID(subOrder.getOrderID());
            var brand = designDetail
                    .stream()
                    .map(DesignDetail::getBrand)
                    .findFirst();

            var completedAheadOfSchedule = 0;
            var completedLate = 0;
            var subOrderStageList = stageService.getOrderStageByOrderID(subOrder.getOrderID());
            var brandOrderRating = orderRating;

            for (var subOrderStage : subOrderStageList) {

                if (subOrderStage.getLastModifiedDate() == null) continue;

                LocalDateTime lastModifiedDateTimeFormatted = LocalDateTime.parse(subOrderStage.getLastModifiedDate(), inputFormatter);

                // Compare the stages and calculate counters based on dates
                if (subOrderStage.getStage().equals(OrderStatus.FINISH_FIRST_STAGE)) {
                    if (lastModifiedDateTimeFormatted.isBefore(estimatedDateFinishFirstStage)) {
                        completedAheadOfSchedule++;
                    } else if (lastModifiedDateTimeFormatted.isAfter(estimatedDateFinishFirstStage)) {
                        completedLate++;
                    }
                    logger.error("Last Modified Date Time At Finish First Stage: {}", lastModifiedDateTimeFormatted);
                } else if (subOrderStage.getStage().equals(OrderStatus.FINISH_SECOND_STAGE)) {
                    if (lastModifiedDateTimeFormatted.isBefore(estimatedDateFinishSecondStage)) {
                        completedAheadOfSchedule++;
                    } else if (lastModifiedDateTimeFormatted.isAfter(estimatedDateFinishSecondStage)) {
                        completedLate++;
                    }
                    logger.error("Last Modified Date Time At Finish Second Stage: {}", lastModifiedDateTimeFormatted);
                } else if (subOrderStage.getStage().equals(OrderStatus.COMPLETED)) {
                    if (lastModifiedDateTimeFormatted.isBefore(estimatedDateCompletion)) {
                        completedAheadOfSchedule++;
                    } else if (lastModifiedDateTimeFormatted.isAfter(estimatedDateCompletion)) {
                        completedLate++;
                    }
                    logger.error("Last Modified Date Time At Finish Complete Stage: {}", lastModifiedDateTimeFormatted);
                }
            }

            // Update orderRating based on the completion status
            var systemPropertyRateAHeadSchedule = systemPropertiesService.getByName("RATE_AHEAD_SCHEDULE");
            var rateAHeadSchedule = Double.parseDouble(systemPropertyRateAHeadSchedule.getPropertyValue());

            var systemPropertyRateLateSchedule = systemPropertiesService.getByName("RATE_LATE_SCHEDULE");
            var rateLateSchedule = Double.parseDouble(systemPropertyRateLateSchedule.getPropertyValue());

            brandOrderRating += (completedAheadOfSchedule > 0) ? (float) (completedAheadOfSchedule * rateAHeadSchedule) : 0;
            brandOrderRating += (completedLate > 0) ? (float) (completedLate * -rateLateSchedule) : 0;
            if (brandOrderRating <= 0) brandOrderRating = 0.0f;
            else if (brandOrderRating > 5) brandOrderRating = 5.0f;

            logger.warn("Brand {} Complete A Head of Schedule {}", brand.get().getUser().getEmail(), completedAheadOfSchedule);
            logger.warn("Brand {} Complete Late {}", brand.get().getUser().getEmail(), completedLate);
            logger.warn("Order Rating {}", brandOrderRating);
            // Update the rating for the brand
            brandService.ratingBrand(brand.get().getBrandID(), 1, brandOrderRating);
        }
    }


    @Override
    public OrderTimeLineResponse getOrderTimeLineByParentOrderID(String parentOrderID) {
        var parentOrder = orderRepository.findById(parentOrderID)
                .orElseThrow(() -> new ItemNotFoundException("Can not find Order with Parent Order ID: " + parentOrderID));


        var subOrderList = getSubOrderByParentID(parentOrderID);
        var maximumDateAtFirstStage = -1;
        var maximumDateAtSecondStage = -1;
        var maximumDateAtCompleteStage = -1;
        for (var subOrder : subOrderList) {
            var designDetail = detailRepository.getDesignDetailBySubOrderID(subOrder.getOrderID());
            var brand = designDetail
                    .stream()
                    .map(DesignDetail::getBrand)
                    .findFirst();

            var systemPropertiesResponse = systemPropertiesService.getByName("BRAND_PRODUCTIVITY");
            var brandProductivity = brandPropertiesService.getByBrandIDAndPropertyID(brand.get().getBrandID(), systemPropertiesResponse.getPropertyID());
            // Get All Stage Of SubOrder
            // Calculate All Quantity At FirstStage, SecondStage, CompleteStage
            // Find the Maximum Date At FirstStage, SecondStage, CompleteStage
            logger.info("Finish First Stage");
            logger.info("Brand {} Brand Quantity {} Brand Property {}", brand.get().getUser().getEmail(), Utilities.roundToNearestHalf(subOrder.getQuantity() * 1.0 / 3), brandProductivity.getBrandPropertyValue());
            maximumDateAtFirstStage = Math.max(maximumDateAtFirstStage, (int) Math.ceil((subOrder.getQuantity() * 1.0 / 3) / Integer.parseInt(brandProductivity.getBrandPropertyValue())));
            logger.info("Finish Second Stage");
            logger.info("Brand {} Brand Quantity {} Brand Property {}", brand.get().getUser().getEmail(), Utilities.roundToNearestHalf(subOrder.getQuantity() * 2.0 / 3), brandProductivity.getBrandPropertyValue());
            maximumDateAtSecondStage = Math.max(maximumDateAtSecondStage, (int) Math.ceil((subOrder.getQuantity() * 2.0 / 3) / Integer.parseInt(brandProductivity.getBrandPropertyValue())));
            logger.info("Finish Complete Stage");
            logger.info("Brand {} Brand Quantity {} Brand Property {}", brand.get().getUser().getEmail(), Utilities.roundToNearestHalf(subOrder.getQuantity() * 1.0), brandProductivity.getBrandPropertyValue());
            maximumDateAtCompleteStage = Math.max(maximumDateAtCompleteStage, (int) Math.ceil((subOrder.getQuantity() * 1.0) / Integer.parseInt(brandProductivity.getBrandPropertyValue())));
        }

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return OrderTimeLineResponse
                .builder()
                .estimatedQuantityFinishFirstStage(Utilities.roundToNearestHalf(parentOrder.getQuantity() * 1.0 / 3))
                .estimatedDateFinishFirstStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtFirstStage)))
                .estimatedQuantityFinishSecondStage(Utilities.roundToNearestHalf(parentOrder.getQuantity() * 2.0 / 3))
                .estimatedDateFinishSecondStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtSecondStage)))
                .estimatedQuantityFinishCompleteStage(parentOrder.getQuantity())
                .estimatedDateFinishCompleteStage(dateTimeFormatter.format(parentOrder.getExpectedStartDate().plusDays(maximumDateAtCompleteStage)))
                .build();
    }

    @Override
    public List<FullOrderResponse> getFullProp() throws JsonProcessingException {
        try {
            var listOrder = orderRepository.findAll()
                    .stream()
                    .filter(
                            order -> order.getOrderType().equals("PARENT_ORDER") &&
                                    (order.getOrderStatus() == OrderStatus.CANCEL
                                            ||
                                            order.getOrderStatus() == OrderStatus.DELIVERED)
                    )
                    .toList();
            List<FullOrderResponse> response = new ArrayList<>();
            for (Order order : listOrder) {
                FullOrderResponse fullOrderResponse = orderMapper.mapToFullOrderResponse(order);
                response.add(fullOrderResponse);
            }
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<FullOrderResponse> getFullPropByBrandID(String brandID) throws JsonProcessingException {
        try {
            var listOrder = orderRepository.findAll()
                    .stream()
                    .filter(
                            order -> order.getOrderType().equals("SUB_ORDER")
                                    &&
                                    (order.getOrderStatus() == OrderStatus.CANCEL
                                            ||
                                            order.getOrderStatus() == OrderStatus.COMPLETED) &&
                                    order.getDetailList() != null
                                    &&
                                    !order.getDetailList().isEmpty()
                                    &&
                                    order.getDetailList().get(0).getBrand() != null
                                    &&
                                    order.getDetailList().get(0).getBrand().getBrandID().equals(brandID)
                                    &&
                                    order.getPaymentList() != null
                                    &&
                                    !order.getPaymentList().isEmpty()
                    )
                    .toList();
            List<FullOrderResponse> response = new ArrayList<>();
            for (Order order : listOrder) {
                FullOrderResponse fullOrderResponse = orderMapper.mapToFullOrderResponse(order);
                response.add(fullOrderResponse);
            }
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public Boolean isCreateShippingOrder(String parentOrderID) {
        return orderRepository.findOrderByParentOrderID(parentOrderID).getLabelID() != null;
    }
}
