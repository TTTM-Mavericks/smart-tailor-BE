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
public class SizeExpertTailoringResponse {
    private UUID expertTailoringID;

    private String expertTailoringName;

    private UUID sizeID;

    private String sizeName;

    private Double minFabric;

    private Double maxFabric;

    private String unit;

    private String createDate;

    private String lastModifiedDate;
}
