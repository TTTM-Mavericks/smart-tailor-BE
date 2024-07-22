package com.smart.tailor.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.smart.tailor.entities.Order;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.service.PaymentService;
import com.smart.tailor.utils.response.OrderCustomResponse;
import com.smart.tailor.utils.response.OrderResponse;
import com.smart.tailor.utils.response.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderMapper {
    OrderResponse mapToOrderResponse(Order order) throws Exception;

    OrderCustomResponse mapToOrderCustomResponse(Order order) throws Exception;
}

@Component
@RequiredArgsConstructor
class OrderMapperImpl implements OrderMapper {
    private final DesignService designService;
    private final DesignDetailMapper detailMapper;
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final Logger logger = LoggerFactory.getLogger(OrderMapperImpl.class);

    @Transactional(readOnly = true)
    @Override
    public OrderResponse mapToOrderResponse(Order order) throws JsonProcessingException {
        if (order == null) {
            return null;
        }

        OrderResponse.OrderResponseBuilder orderResponse = OrderResponse.builder();
        orderResponse.parentOrderID(order.getParentOrder() != null ? order.getParentOrder().getOrderID() : null);
        orderResponse.orderType(order.getOrderType());
        orderResponse.orderID(order.getOrderID());
        orderResponse.quantity(order.getQuantity());
        orderResponse.orderStatus(order.getOrderStatus());
        orderResponse.address(order.getAddress());
        orderResponse.province(order.getProvince());
        orderResponse.district(order.getDistrict());
        orderResponse.ward(order.getWard());
        orderResponse.phone(order.getPhone());
        orderResponse.buyerName(order.getBuyerName());
        orderResponse.totalPrice(order.getTotalPrice());
        orderResponse.expectedStartDate(order.getExpectedStartDate());
        orderResponse.expectedProductCompletionDate(order.getExpectedProductCompletionDate());
        orderResponse.estimatedDeliveryDate(order.getEstimatedDeliveryDate());
        orderResponse.productionStartDate(order.getProductionStartDate());
        orderResponse.productionCompletionDate(order.getProductionCompletionDate());
        orderResponse.detailList(
                order.getDetailList() != null ? order.getDetailList().stream().map(
                        detailMapper::mapperToDesignDetailResponse
                ).toList() : null
        );
        try {
            List<PaymentResponse> paymentResponseList = paymentService.findAllByOrderID(order.getOrderID())
                    .stream()
                    .map(paymentMapper::mapperToPaymentResponse)
                    .toList();
            if (!paymentResponseList.isEmpty()) {
                orderResponse.paymentList(paymentResponseList);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return orderResponse.build();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderCustomResponse mapToOrderCustomResponse(Order order) throws JsonProcessingException {
        if (order == null) {
            return null;
        }

        OrderCustomResponse.OrderCustomResponseBuilder orderResponse = OrderCustomResponse.builder();
        orderResponse.designResponse(designService.getDesignByOrderID(order.getOrderID()));
        orderResponse.parentOrderID(order.getParentOrder() != null ? order.getParentOrder().getOrderID() : null);
        orderResponse.orderType(order.getOrderType());
        orderResponse.orderID(order.getOrderID());
        orderResponse.quantity(order.getQuantity());
        orderResponse.orderStatus(order.getOrderStatus());
        orderResponse.address(order.getAddress());
        orderResponse.province(order.getProvince());
        orderResponse.district(order.getDistrict());
        orderResponse.ward(order.getWard());
        orderResponse.phone(order.getPhone());
        orderResponse.buyerName(order.getBuyerName());
        orderResponse.totalPrice(order.getTotalPrice());
        orderResponse.expectedStartDate(order.getExpectedStartDate());
        orderResponse.expectedProductCompletionDate(order.getExpectedProductCompletionDate());
        orderResponse.estimatedDeliveryDate(order.getEstimatedDeliveryDate());
        orderResponse.productionStartDate(order.getProductionStartDate());
        orderResponse.productionCompletionDate(order.getProductionCompletionDate());
        orderResponse.createDate(order.getCreateDate().toString());
        orderResponse.detailList(
                order.getDetailList() != null ? order.getDetailList().stream().map(detailMapper::mapperToDesignDetailResponse).toList() : null
        );
        try {
            List<PaymentResponse> paymentResponseList = paymentService.findAllByOrderID(order.getOrderID())
                    .stream()
                    .map(paymentMapper::mapperToPaymentResponse)
                    .toList();
            if (!paymentResponseList.isEmpty()) {
                orderResponse.paymentList(paymentResponseList);
            }
            orderResponse.paymentList(paymentResponseList);
        } catch (Exception ex) {
            throw ex;
        }
        return orderResponse.build();
    }
}