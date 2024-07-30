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
public class SampleProductDataResponse {
    private UUID sampleModelID;

    private UUID orderID;

    private UUID brandID;

    private String description;

    private String imageUrl;

    private String video;

    private String createDate;

    private String lastModifiedDate;
}
