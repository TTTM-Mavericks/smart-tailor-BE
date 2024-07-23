package com.smart.tailor.utils.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignRequest {
    @NotNull(message = "PartOfDesignName is not null")
    @NotBlank(message = "PartOfDesignName is not blank")
    private String partOfDesignName;

    @NotNull(message = "PartOfDesign imageUrl is not null")
    @NotBlank(message = "PartOfDesign imageUrl is not blank")
    private String imageUrl;

    private String materialID;

    private String successImageUrl;

    private String realPartImageUrl;

    @NotNull(message = "width is required")
    @Min(value = 1, message = "width have to greater than 0")
    private Integer width;

    @NotNull(message = "height is required")
    @Min(value = 1, message = "height have to greater than 0")
    private Integer height;

    @Valid
    private List<@Valid ItemMaskRequest> itemMask;
}
