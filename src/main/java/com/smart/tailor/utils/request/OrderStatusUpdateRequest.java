package com.smart.tailor.utils.request;

import com.smart.tailor.enums.OrderStatus;
import com.smart.tailor.enums.PrintType;
import com.smart.tailor.validate.ValidEnumValue;
import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateRequest {
    @NotBlank(message = "Order ID is required")
    @ValidStringUUID(message = "Order ID must be a valid UUID")
    private String orderID;

    @NotBlank(message = "Status is required")
    @ValidEnumValue(name = "status", enumClass = OrderStatus.class)
    private String status;
}
