package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandMaterialResponse {
    private UUID brandID;

    private UUID materialID;

    private String categoryName;

    private String materialName;

    private Double hsCode;

    private String unit;

    private Integer basePrice;

    private Integer brandPrice;

    private String createDate;

    private String lastModifiedDate;
}
