package com.smart.tailor.mapper;

import com.smart.tailor.entities.Order;
import com.smart.tailor.service.DesignService;
import com.smart.tailor.utils.response.OrderCustomResponse;
import com.smart.tailor.utils.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public interface OrderMapper {
    OrderResponse mapToOrderResponse(Order order);

    OrderCustomResponse mapToOrderCustomeResponse(Order order);
}

@Component
@RequiredArgsConstructor
class OrderMapperImpl implements OrderMapper {
    private final DesignService designService;
    private final DesignDetailMapper detailMapper;

    @Override
    public OrderResponse mapToOrderResponse(Order order) {
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
                order.getDetailList() != null ? order.getDetailList().stream().map(detailMapper::mapperToDesignDetailResponse).toList() : null
        );
        return orderResponse.build();
    }

    @Override
    public OrderCustomResponse mapToOrderCustomeResponse(Order order) {
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
        orderResponse.detailList(
                order.getDetailList() != null ? order.getDetailList().stream().map(detailMapper::mapperToDesignDetailResponse).toList() : null
        );
        return orderResponse.build();
    }
}