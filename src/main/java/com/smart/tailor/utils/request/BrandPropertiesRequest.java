package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandPropertiesRequest {
    private UUID brandID;
    private UUID systemPropertyID;
    private String brandPropertyValue;
    private Boolean brandPropertyStatus;
}
