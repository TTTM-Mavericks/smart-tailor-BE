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
public class SizeExpertTailoringRequest {
    @NotBlank(message = "expertTailoringName can not be blank")
    @NotNull(message = "expertTailoringName can not be null")
    private String expertTailoringName;

    @NotBlank(message = "sizeName can not be blank")
    @NotNull(message = "sizeName can not be null")
    private String sizeName;

    @NotNull(message = "minFabric is required")
    @Min(value = 0, message = "minFabric can not less than 0")
    private Double minFabric;

    @NotNull(message = "maxFabric is required")
    @Min(value = 0, message = "maxFabric can not less than 0")
    private Double maxFabric;

    @NotBlank(message = "unit can not be blank")
    @NotNull(message = "unit can not be null")
    private String unit;
}
