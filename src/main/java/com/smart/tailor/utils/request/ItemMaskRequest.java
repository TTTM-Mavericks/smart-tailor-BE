package com.smart.tailor.utils.request;

import com.smart.tailor.enums.PrintType;
import com.smart.tailor.validate.ValidEnumValue;
import jakarta.validation.constraints.NotBlank;
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
public class ItemMaskRequest {
    private String itemMaskName;

    private String typeOfItem;

    @NotEmpty(message = "materialName is not empty")
    @NotBlank(message = "materialName is not blank")
    private String materialName;

    @NotNull(message = "isSystemItem is required")
    private Boolean isSystemItem;

    @NotNull(message = "positionX is required")
    private Float positionX;

    @NotNull(message = "positionY is required")
    private Float positionY;

    @NotNull(message = "scaleX is required")
    private Float scaleX;

    @NotNull(message = "scaleY is required")
    private Float scaleY;

    @NotNull(message = "indexZ is required")
    private Integer indexZ;

    @NotEmpty(message = "imageUrl is not empty")
    @NotBlank(message = "imageUrl is required")
    private String imageUrl;

    @NotEmpty(message = "printType is not empty")
    @NotBlank(message = "printType is required")
    @ValidEnumValue(name = "printType", enumClass = PrintType.class)
    private String printType;
}