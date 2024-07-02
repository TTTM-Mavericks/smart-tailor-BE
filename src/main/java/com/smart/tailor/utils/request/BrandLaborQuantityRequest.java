package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import com.smart.tailor.validate.ValidUUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandLaborQuantityRequest {
    @ValidStringUUID(message = "laborQuantityID is not type of UUID")
    private String laborQuantityID;

    @NotNull(message = "brandLaborCostPerQuantity is required")
    @Min(value = 0, message = "brandLaborCostPerQuantity can not less than 0")
    private Double brandLaborCostPerQuantity;
}
