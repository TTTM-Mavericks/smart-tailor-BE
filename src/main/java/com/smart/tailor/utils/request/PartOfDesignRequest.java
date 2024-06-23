package com.smart.tailor.utils.request;

import jakarta.validation.Valid;
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

    private String successImageUrl;

    @Valid
    private List<@Valid ItemMaskRequest> itemMaskList;
}
