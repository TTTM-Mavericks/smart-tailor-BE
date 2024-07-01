package com.smart.tailor.utils.request;

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
    @ValidUUID(message = "laborQuantityID is not type of UUID")
    private UUID laborQuantityID;

    @NotNull(message = "laborCostPerQuantity is required")
    @Min(value = 0, message = "laborCostPerQuantity can not less than 0")
    private Double laborCostPerQuantity;
}
