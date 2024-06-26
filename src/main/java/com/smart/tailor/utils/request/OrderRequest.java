package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private UUID parentOrderID;
    private UUID designID;
    private UUID brandID;
    private Integer quantity;
    private UUID discountID;
    private String orderType;
    private String address;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String buyerName;
}
