package com.smart.tailor.utils.response;

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
public class DesignCustomResponse {
    private UUID designID;

    private UserResponse user;

    private ExpertTailoringResponse expertTailoring;

    private String titleDesign;

    private Boolean publicStatus;

    private Float minWeight;

    private Float maxWeight;

    private String imageUrl;

    private String color;

    private String createDate;

    private String lastModifiedDate;
}
