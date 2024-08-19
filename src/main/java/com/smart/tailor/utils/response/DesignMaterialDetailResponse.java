package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignMaterialDetailResponse {
    private String detailName; // It can be PartOfDesignName, ItemMaskName, BrandLaborQuantity

    private BigDecimal meterSquare;

    private BigDecimal minPriceMaterial;

    private BigDecimal maxPriceMaterial;
}
