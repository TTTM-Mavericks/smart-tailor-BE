package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignDetailRequest {
    private UUID designId;
    private UUID orderId;
    List<DesignDetailSize> sizeList;
    private Boolean detailStatus;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
}
