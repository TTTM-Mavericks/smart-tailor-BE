package com.smart.tailor.utils.request;

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

    private Boolean isSystemItem;

    private Boolean isPremium;

    private Float positionX;

    private Float positionY;

    private Float scaleX;

    private Float scaleY;

    private Integer indexZ;

    private String imageUrl;

    private String printType;
}