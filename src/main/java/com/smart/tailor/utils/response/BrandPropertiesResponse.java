package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandPropertiesResponse {
    private UUID brandPropertyID;
    private BrandResponse brand;
    private SystemPropertiesResponse systemProperty;
    private String brandPropertyValue;
    private Boolean brandPropertyStatus;
}
