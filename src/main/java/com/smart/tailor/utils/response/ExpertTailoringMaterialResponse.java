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
public class ExpertTailoringMaterialResponse {
    private UUID categoryID;

    private String categoryName;

    private UUID materialID;

    private String materialName;

    private UUID expertTailoringID;

    private String expertTailoringName;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
