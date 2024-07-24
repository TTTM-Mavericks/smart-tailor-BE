package com.smart.tailor.utils.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequest {
    @NotBlank(message = "Category Name must not be blank")
    @Size(max = 50, message = "Category Name must not exceed 50 characters")
    private String categoryName;

    @NotBlank(message = "Material Name must not be blank")
    @Size(max = 50, message = "Material Name must not exceed 50 characters")
    private String materialName;

    @NotNull(message = "HS Code is required")
    @Min(value = 0, message = "HS Code cannot be less than 0")
    private Long hsCode;

    @NotBlank(message = "Unit must not be blank")
    @Size(max = 50, message = "Unit must not exceed 50 characters")
    private String unit;

    @NotNull(message = "Base Price is required")
    @Min(value = 0, message = "Base Price cannot be less than 0")
    private Integer basePrice;

    @Override
    public String toString() {
        return  "categoryName='" + categoryName + '\'' +
                ", materialName='" + materialName + '\'' +
                ", hsCode=" + hsCode +
                ", unit='" + unit + '\'' +
                ", basePrice=" + basePrice ;
    }
}
