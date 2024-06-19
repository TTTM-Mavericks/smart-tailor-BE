package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandMaterialRequest {
    private String brandName;

    private String categoryName;

    private String materialName;

    private Double hsCode;

    private String unit;

    private Double basePrice;

    private Double brandPrice;

    @Override
    public String toString() {
        return
                "brandName='" + brandName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", hsCode='" + hsCode + '\'' +
                ", unit='" + unit + '\'' +
                ", basePrice=" + basePrice + '\'' +
                ", brandPrice=" + brandPrice;
    }
}
