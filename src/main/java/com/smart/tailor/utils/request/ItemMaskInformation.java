package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemMaskInformation {
    private Float scaleX;

    private Float scaleY;

    private UUID materialID;

    private String materialName;
}
