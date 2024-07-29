package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private UUID orderID;
    private UUID parentOrderID;
    private Integer quantity;
    private OrderStatus orderStatus;
    private String orderType;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
    private Integer totalPrice;
    private String expectedStartDate;
    private String expectedProductCompletionDate;
    private String estimatedDeliveryDate;
    private String productionStartDate;
    private String productionCompletionDate;
    private List<DesignDetailResponse> detailList;
    private List<PaymentResponse> paymentList;
}
