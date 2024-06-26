package com.smart.tailor.service;

import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.response.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void createOrder(OrderRequest orderRequest);

    void updateOrderStatus(UUID orderID, String orderStatus);

    OrderResponse getOrderByOrderID(UUID orderID);

    List<OrderResponse> getOrderByBrandID(UUID brandID);

    List<OrderResponse> getOrderByDesignID(UUID designID);

    List<OrderResponse> getSubOrderByParentID(UUID parentOrderID);
}
