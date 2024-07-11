package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Employee;
import com.smart.tailor.entities.Order;
import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.exception.BadRequestException;
import com.smart.tailor.mapper.OrderMapper;
import com.smart.tailor.repository.OrderRepository;
import com.smart.tailor.service.BrandService;
import com.smart.tailor.service.CustomerService;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.OrderService;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.response.CustomerResponse;
import com.smart.tailor.utils.response.DesignResponse;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final BrandService brandService;
    private final DesignService designService;
    private final CustomerService customerService;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
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
            if (!Utilities.isStringNotNullOrEmpty(orderRequest.getBuyerName())) {
                buyerName = orderRequest.getBuyerName();
            } else {
                buyerName = customerResponse.getFullName();
            }

            String orderType = orderRequest.getOrderType();

            Order order = Order.builder()
                    .quantity(quantity)
                    .address(address)
                    .province(province)
                    .district(district)
                    .ward(ward)
                    .orderType(orderType)
                    .phone(phone)
                    .buyerName(buyerName)
                    .orderStatus(OrderStatus.PENDING)
                    .build();
            var orderResponse = orderRepository.save(order);
            return orderMapper.mapToOrderResponse(orderResponse);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public List<OrderResponse> getParentOrderByDesignID(UUID designID) {
        List<Order> orderList = orderRepository.findParentOrderByDesignID(designID);
        List<OrderResponse> orderResponse = new ArrayList<>();
        for (Order order : orderList) {
            var response = orderMapper.mapToOrderResponse(order);
//            response.setDesignResponse(
//                    designService.getDesignResponseByID(designID)
//            );
            orderResponse.add(response);
        }
        ;

        return orderResponse;
    }

    @Override
    public void updateOrderStatus(UUID orderID, String orderStatus) {

    }

    @Override
    public OrderResponse getOrderByOrderID(UUID orderID) {
        var order = orderRepository.findById(orderID).isPresent() ? orderRepository.findById(orderID).get() : null;
        var response = orderMapper.mapToOrderResponse(order);
        return response;
    }

    @Override
    public Optional<Order> getOrderById(UUID orderID) {
        return orderRepository.findById(orderID);
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

    @Override
    public List<OrderResponse> getAllOrder() {
        return orderRepository.findAll().stream().map(orderMapper::mapToOrderResponse).toList();
    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }
}
