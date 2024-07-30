package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SampleProductDataRequest {
    @NotBlank(message = "Sub Order ID is required")
    @ValidStringUUID(message = "Sub Order ID must be a valid UUID")
    private String subOrderID;

    @NotBlank(message = "Brand ID is required")
    @ValidStringUUID(message = "Brand ID must be a valid UUID")
    private String brandID;

    private String description;

    private String imageUrl;

    private String video;
}
