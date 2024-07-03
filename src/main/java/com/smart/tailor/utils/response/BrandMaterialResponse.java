package com.smart.tailor.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandMaterialResponse {
    private String brandName;

    private String categoryName;

    private String materialName;

    private Double hsCode;

    private String unit;

    private Double basePrice;

    private Double brandPrice;

    private String createDate;

    private String lastModifiedDate;
}
