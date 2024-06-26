package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private UUID orderID;
    private UUID parentOrderID;
    private DesignResponse designResponse;
    private BrandResponse brandResponse;
    private Integer quantity;
    private DiscountResponse discountResponse;
    private OrderStatus orderStatus;
    private String orderType;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
    private Double totalPrice;
    private LocalDateTime expectedStartDate;
    private LocalDateTime expectedProductCompletionDate;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime productionStartDate;
    private LocalDateTime productionCompletionDate;
}
