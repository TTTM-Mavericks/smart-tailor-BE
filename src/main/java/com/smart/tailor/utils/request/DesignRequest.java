package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidEmail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignRequest {
    @NotNull(message = "userEmail is not null")
    @NotBlank(message = "userEmail is not blank")
    @ValidEmail(message = "userEmail is invalid email address")
    private String userEmail;

    @NotNull(message = "expertTailoringName is not null")
    @NotBlank(message = "expertTailoringName is not blank")
    private String expertTailoringName;

    @NotNull(message = "titleDesign is not null")
    @NotBlank(message = "titleDesign is not blank")
    private String titleDesign;

    @NotNull(message = "publicStatus is required")
    private Boolean publicStatus;

    private String imageUrl;

    private String color;

    @Valid
    private List<@Valid PartOfDesignRequest> partOfDesign;
}
