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
public class ReportImageResponse {
    private UUID reportImageID;

    private String reportImageName;

    private String reportImageUrl;

    private String createDate;

    private String lastModifiedDate;
}
