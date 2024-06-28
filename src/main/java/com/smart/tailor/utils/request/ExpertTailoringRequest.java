package com.smart.tailor.utils.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertTailoringRequest {
    @NotNull(message = "expertTailoringName is not null")
    @NotEmpty(message = "expertTailoringName is not empty")
    private String expertTailoringName;

    @NotNull(message = "sizeImageUrl is not null")
    @NotEmpty(message = "sizeImageUrl is not empty")
    private String sizeImageUrl;
}
