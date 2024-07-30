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
    @NotBlank(message = "Order ID is required")
    @ValidStringUUID(message = "Order ID must be a valid UUID")
    private String orderID;

    @NotBlank(message = "Brand ID is required")
    @ValidStringUUID(message = "Brand ID must be a valid UUID")
    private String brandID;

    private String description;

    private String imageUrl;

    private String video;
}
