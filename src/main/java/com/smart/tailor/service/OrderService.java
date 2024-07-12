package com.smart.tailor.service;

import com.smart.tailor.entities.Order;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.response.OrderCustomResponse;
import com.smart.tailor.utils.response.OrderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getParentOrderByDesignID(UUID designID);

    void updateOrderStatus(UUID orderID, String orderStatus);

    OrderCustomResponse getOrderByOrderID(UUID orderID);

    Optional<Order> getOrderById(UUID orderID);

    List<OrderResponse> getOrderByBrandID(UUID brandID);

    List<OrderResponse> getOrderByDesignID(UUID designID);

    List<OrderResponse> getSubOrderByParentID(UUID parentOrderID);

    List<OrderResponse> getAllOrder();

    void updateOrder(Order order);
}
