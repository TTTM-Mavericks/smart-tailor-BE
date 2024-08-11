package com.smart.tailor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.entities.Order;
import com.smart.tailor.utils.request.OrderPickingRequest;
import com.smart.tailor.utils.request.OrderRequest;
import com.smart.tailor.utils.request.OrderStatusUpdateRequest;
import com.smart.tailor.utils.request.RatingOrderRequest;
import com.smart.tailor.utils.response.*;

import java.util.List;
import java.util.Optional;


public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest) throws Exception;

    List<OrderResponse> getParentOrderByDesignID(String designID) throws Exception;

//    void updateOrderStatus(String orderID, String orderStatus);

    OrderCustomResponse getOrderByOrderID(String orderID) throws Exception;

    OrderCustomResponse getOrderDetailByOrderID(String orderID) throws Exception;

    Optional<Order> getOrderById(String orderID);

    List<OrderCustomResponse> getOrderByBrandID(String brandID) throws Exception;

    List<OrderResponse> getOrderByDesignID(String designID);

    List<OrderCustomResponse> getOrderByUserID(String userID) throws Exception;

    List<OrderResponse> getSubOrderByParentID(String parentOrderID);

    List<OrderResponse> getAllOrder();

    OrderResponse changeOrderStatus(OrderStatusUpdateRequest orderRequest) throws Exception;

    void updateOrder(Order order) throws Exception;

    OrderResponse brandPickOrder(OrderPickingRequest orderPickingRequest) throws Exception;

    Order getOrderByDetailID(String detailID);

    Boolean isOrderCompletelyPicked(String orderID);

    Boolean isOrderExpireTime(String orderID);

    List<OrderResponse> getAllParentOrder();

    List<String> filterBrandForSpecificOrderBaseOnDesign(String designID);

    void confirmOrder(String orderID);

    List<OrderStageResponse> getOrderStageByOrderID(String orderID);

    void ratingOrder(RatingOrderRequest ratingOrderRequest);

    OrderTimeLineResponse getOrderTimeLineByParentOrderID(String parentOrderID);

    List<FullOrderResponse> getFullProp() throws JsonProcessingException;

    List<FullOrderResponse> getFullPropByBrandID(String brandID) throws JsonProcessingException;

    Boolean isCreateShippingOrder(String parentOrderID);

    OrderDetailShippingResponse getOrderDetailShippingResponseByLabelID(String labelID);
}
