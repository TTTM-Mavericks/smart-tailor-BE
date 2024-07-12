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
public class MaterialWithPriceResponse {
    private UUID materialID;

    private String materialName;

    private String categoryName;

    private Long hsCode;

    private String unit;

    private Double basePrice;

    private Double minPrice;

    private Double maxPrice;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
