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

    private String materialName;

    private String categoryName;

    private String unit;

    private Double price;

    @Override
    public String toString() {
        return
            "brandName='" + brandName + '\'' +
            ", materialName='" + materialName + '\'' +
            ", categoryName='" + categoryName + '\'' +
            ", unit='" + unit + '\'' +
            ", price=" + price;
    }

}
