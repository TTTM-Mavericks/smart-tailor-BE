package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignDetailRequest {
    private UUID designId;
    private UUID orderId;
    private UUID brandId;
    private Integer quantity;
    private String size;
    private Boolean detailStatus;
}
