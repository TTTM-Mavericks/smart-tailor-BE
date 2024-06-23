package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequest {
    private String categoryName;

    private String materialName;

    private Double hsCode;

    private String unit;

    private Double basePrice;
}
