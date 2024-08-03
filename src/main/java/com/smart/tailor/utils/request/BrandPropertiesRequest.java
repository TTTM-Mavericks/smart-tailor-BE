package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandPropertiesRequest {
    @NotNull(message = "Brand ID is required")
    @ValidStringUUID(message = "Brand ID must be a valid UUID")
    private String brandID;

    @NotNull(message = "System Property ID is required")
    @ValidStringUUID(message = "System Property ID must be a valid UUID")
    private String systemPropertyID;

    @NotBlank(message = "Brand Property Value is required")
    @Size(max = 150, message = "Brand Property Value must not exceed 150 characters")
    private String brandPropertyValue;

    @NotNull(message = "Brand Property Status is required")
    private Boolean brandPropertyStatus;
}
