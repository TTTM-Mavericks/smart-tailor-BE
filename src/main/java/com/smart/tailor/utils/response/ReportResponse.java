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
public class ReportResponse {
    private UUID reportID;

    private String typeOfReport;

    private OrderResponse orderResponse;

    private String content;

    private Boolean reportStatus;

    private List<ReportImageResponse> reportImageList;

    private String createDate;

    private String lastModifiedDate;
}
