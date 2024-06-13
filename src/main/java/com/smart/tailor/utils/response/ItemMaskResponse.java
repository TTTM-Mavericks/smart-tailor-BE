package com.smart.tailor.utils.response;

import com.smart.tailor.enums.PrintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMaskResponse {
    private String itemMaskName;

    private String typeOfItem;

    private Boolean isSystemItem;

    private Boolean isPremium;

    private Float positionX;

    private Float positionY;

    private Float scaleX;

    private Float scaleY;

    private Float zIndex;

    private String imageUrl;

    private PrintType printType;
}
