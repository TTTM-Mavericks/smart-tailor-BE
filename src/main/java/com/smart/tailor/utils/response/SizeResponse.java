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
public class SizeResponse {
    private UUID sizeID;

    private String sizeName;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
