package com.smart.tailor.utils.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandMaterialRequest {
    @NotNull(message = "brandName is not null")
    @NotBlank(message = "brandName is not blank")
    private String brandName;

    @NotNull(message = "categoryName is not null")
    @NotBlank(message = "categoryName is not blank")
    private String categoryName;

    @NotNull(message = "materialName is not null")
    @NotBlank(message = "materialName is not blank")
    private String materialName;

    @NotNull(message = "hsCode is required")
    @Min(value = 0, message = "hsCode can not less than 0")
    private Long hsCode;

    @NotNull(message = "unit is not null")
    @NotBlank(message = "unit is not blank")
    private String unit;

    @NotNull(message = "basePrice is required")
    @Min(value = 0, message = "basePrice can not less than 0")
    private Double basePrice;

    @NotNull(message = "brandPrice is required")
    @Min(value = 0, message = "brandPrice can not less than 0")
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
