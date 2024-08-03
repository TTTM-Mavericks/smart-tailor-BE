package com.smart.tailor.utils.response;

import com.smart.tailor.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleProductDataResponse {
    OrderStatus stage;
    Boolean status;
    private UUID sampleModelID;
    private UUID orderID;
    private UUID orderStageID;
    private UUID brandID;
    private String brandName;
    private String description;

    private String imageUrl;

    private String video;

    private String createDate;

    private String lastModifiedDate;
}
