package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private UUID productID;
    private DesignResponse design;
    private BrandResponse brand;
    private String productName;
    private Double pricePerProduct;
    private Boolean gender;
    private Integer rating;
    private String description;
    private Boolean publicStatus;
    private String size;
    private Boolean productStatus;
}
