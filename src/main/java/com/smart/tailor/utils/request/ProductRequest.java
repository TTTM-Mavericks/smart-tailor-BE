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
public class ProductRequest {
    private UUID productID;
    private UUID designID;
    private UUID brandID;
    private String productName;
    private Double pricePerProduct;
    private Boolean gender;
    private Integer rating;
    private String description;
    private Boolean publicStatus;
    private String size;
    private Boolean productStatus;
}
