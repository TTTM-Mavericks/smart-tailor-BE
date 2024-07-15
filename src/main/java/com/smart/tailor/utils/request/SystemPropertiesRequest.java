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
public class SystemPropertiesRequest {
    private String propertyName;
    private String propertyUnit;
    private String propertyDetail;
    private String propertyType;
    private String propertyValue;
    private Boolean propertyStatus;
}
