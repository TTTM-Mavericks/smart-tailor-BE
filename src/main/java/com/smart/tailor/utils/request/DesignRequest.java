package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidColor;
import com.smart.tailor.validate.ValidStringUUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignRequest {
    @NotBlank(message = "User ID is required")
    @ValidStringUUID(message = "User ID must be a valid UUID")
    private String userID;

    @NotBlank(message = "Expert Tailoring ID is required")
    @ValidStringUUID(message = "Expert Tailoring ID must be a valid UUID")
    private String expertTailoringID;

    @NotBlank(message = "Title Design is required")
    @Size(max = 50, message = "Title Design must not exceed 50 characters")
    private String titleDesign;

    @NotNull(message = "Public Status is required")
    private Boolean publicStatus;

    @ValidColor
    private String color;

    private String imageUrl;

    @NotEmpty(message = "Part of Design is required")
    @Valid
    private List<@Valid PartOfDesignRequest> partOfDesign;
}
