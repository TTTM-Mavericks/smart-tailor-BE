package com.smart.tailor.utils.request;

import com.smart.tailor.validate.ValidEmail;
import com.smart.tailor.validate.ValidStringUUID;
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
    @NotBlank(message = "userID can not be blank")
    @NotNull(message = "userID can not be null")
    @ValidStringUUID(message = "userID is invalid of type UUID")
    private String userID;

    @NotNull(message = "expertTailoringID is not null")
    @NotBlank(message = "expertTailoringID is not blank")
    @ValidStringUUID(message = "expertTailoringID is invalid of type UUID")
    private String expertTailoringID;

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
