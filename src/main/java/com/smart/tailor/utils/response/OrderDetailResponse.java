package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    DesignDetailResponse designDetail;
    private OrderStatus detailStatus;
    private LocalDateTime expectedStartDate;
    private LocalDateTime expectedProductCompletionDate;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime productionStartDate;
    private LocalDateTime productionCompletionDate;
}
