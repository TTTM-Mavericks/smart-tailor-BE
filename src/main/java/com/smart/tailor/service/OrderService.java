package com.smart.tailor.service;

import com.smart.tailor.entities.Order;
import com.smart.tailor.utils.request.OrderPickingRequest;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.response.OrderCustomResponse;
import com.smart.tailor.utils.response.OrderResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest) throws Exception;

    List<OrderResponse> getParentOrderByDesignID(UUID designID) throws Exception;

    void updateOrderStatus(UUID orderID, String orderStatus);

    OrderCustomResponse getOrderByOrderID(UUID orderID) throws Exception;

    OrderCustomResponse getOrderDetailByOrderID(UUID orderID) throws Exception;

    Optional<Order> getOrderById(UUID orderID);

    List<OrderCustomResponse> getOrderByBrandID(UUID brandID) throws Exception;

    List<OrderResponse> getOrderByDesignID(UUID designID);

    List<OrderCustomResponse> getOrderByUserID(UUID userID) throws Exception;

    List<OrderResponse> getSubOrderByParentID(UUID parentOrderID);

    List<OrderResponse> getAllOrder();

    OrderResponse changeOrderStatus(OrderStatusUpdateRequest orderRequest) throws Exception;

    void updateOrder(Order order);

    OrderResponse brandPickOrder(OrderPickingRequest orderPickingRequest) throws Exception;

    Order getOrderByDetailID(UUID detailID);

    Boolean isOrderCompletelyPicked(UUID orderID);

    Boolean isOrderExpireTime(UUID orderID);

    List<OrderResponse> getAllParentOrder();

    List<String> filterBrandForSpecificOrderBaseOnDesign(UUID designID);

}
