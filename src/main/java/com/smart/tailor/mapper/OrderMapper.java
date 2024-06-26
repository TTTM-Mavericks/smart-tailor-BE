package com.smart.tailor.mapper;

import com.smart.tailor.entities.Order;
import com.smart.tailor.utils.response.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse mapToOrderResponse(Order order);
}
