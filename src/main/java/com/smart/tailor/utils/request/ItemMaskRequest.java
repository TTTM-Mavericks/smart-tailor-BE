package com.smart.tailor.utils.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smart.tailor.enums.PrintType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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