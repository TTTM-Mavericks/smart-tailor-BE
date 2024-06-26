package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Order;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.response.CustomerResponse;
import com.smart.tailor.utils.response.DesignResponse;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BrandService brandService;
    private final DesignService designService;
    private final DiscountService discountService;
    private final CustomerService customerService;

    @Override
    public void createOrder(OrderRequest orderRequest) {
        UUID parentOrderID = orderRequest.getParentOrderID() != null ? orderRequest.getParentOrderID() : null;
        if (!Utilities.isStringNotNullOrEmpty(orderRequest.getDesignID().toString())) {
            throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": designID");
        }
        UUID designID = orderRequest.getDesignID();

        DesignResponse designResponse = designService.getDesignResponseByID(designID);
        if (designResponse == null) {
            throw new BadRequestException(MessageConstant.INVALID_INPUT + ": designID");
        }

        UUID brandID = orderRequest.getBrandID() != null ? orderRequest.getBrandID() : null;
        Integer quantity = orderRequest.getQuantity() != null
                && Utilities.isValidNumber(orderRequest.getQuantity().toString())
                ? orderRequest.getQuantity()
                : null;
        UUID discountID = orderRequest.getDiscountID() != null ? orderRequest.getDiscountID() : null;
        if (!Utilities.isStringNotNullOrEmpty(orderRequest.getOrderType())) {
            throw new BadRequestException(MessageConstant.MISSING_ARGUMENT + ": orderType");
        }

        UserResponse userResponse = designResponse.getUserResponse();
        CustomerResponse customerResponse = customerService.getCustomerByUserID(userResponse.getUserID());

        String address = "";
        String province = "";
        String district = "";
        String ward = "";
        String orderType = orderRequest.getOrderType();
        if (!Utilities.isStringNotNullOrEmpty(orderRequest.getAddress())
                && !Utilities.isStringNotNullOrEmpty(orderRequest.getProvince())
                && !Utilities.isStringNotNullOrEmpty(orderRequest.getDistrict())
                && !Utilities.isStringNotNullOrEmpty(orderRequest.getWard())) {
            address = customerResponse.getAddress();
            province = customerResponse.getProvince();
            district = customerResponse.getDistrict();
            ward = customerResponse.getWard();
        }

        String phone;
        if (orderRequest.getPhone() != null && !Utilities.isValidVietnamesePhoneNumber(orderRequest.getPhone())) {
            throw new BadRequestException(MessageConstant.INVALID_INPUT + ": phone");
        } else if (orderRequest.getPhone() == null) {
            phone = customerResponse.getPhoneNumber();
        } else {
            phone = orderRequest.getPhone();
        }

        String buyerName;
        if (!Utilities.isStringNotNullOrEmpty(orderRequest.getBuyerName())) {
            buyerName = orderRequest.getBuyerName();
        } else {
            buyerName = customerResponse.getFullName();
        }
        Order order = Order.builder()
                .parentOrderID(parentOrderID)
                .designID(designID)
                .brandID(brandID)
                .quantity(quantity)
                .discountID(discountID)
                .address(address)
                .province(province)
                .district(district)
                .ward(ward)
                .orderType(orderType)
                .phone(phone)
                .buyerName(buyerName)
                .build();
        orderRepository.save(
                order
        );
    }

    @Override
    public void updateOrderStatus(UUID orderID, String orderStatus) {

    }

    @Override
    public OrderResponse getOrderByOrderID(UUID orderID) {
        return null;
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
        return null;
    }
}
