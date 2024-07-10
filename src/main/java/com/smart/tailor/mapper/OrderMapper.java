package com.smart.tailor.mapper;

import com.smart.tailor.entities.Order;
import com.smart.tailor.utils.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "order.orderType", target = "orderType")
    OrderResponse mapToOrderResponse(Order order);
}
