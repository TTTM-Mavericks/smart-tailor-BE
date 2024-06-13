package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DesignRequest {
    private String userEmail;

    private String expertTailoringName;

    private String titleDesign;

    private Boolean publicStatus;

    private String imageUrl;

    private String color;

    private List<PartOfDesignRequest> partOfDesignRequestList;
}
