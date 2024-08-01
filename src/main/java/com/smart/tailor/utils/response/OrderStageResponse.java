package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStageResponse {
    String stageId;
    String orderID;
    OrderStatus stage;
    Integer currentQuantity;
    Integer remainingQuantity;
    Boolean status;
    String createdDate;
    String lastModifiedDate;
}
