package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialResponse {
    private UUID materialID;

    private String materialName;

    private String categoryName;

    private Long hsCode;

    private String unit;

    private Integer basePrice;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
