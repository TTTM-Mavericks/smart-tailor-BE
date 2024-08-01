package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.constraints.DecimalMin;
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
public class RatingOrderRequest {
    @NotBlank(message = "User ID is required")
    @ValidStringUUID(message = "User ID must be a valid UUID")
    private String userID;

    @NotBlank(message = "Parent Order ID is required")
    @ValidStringUUID(message = "Parent Order ID must be a valid UUID")
    private String parentOrderID;

    @NotNull(message = "Rating is required")
    @DecimalMin(value = "0.01", message = "Rating must be greater than 0")
    private Float rating;
}
