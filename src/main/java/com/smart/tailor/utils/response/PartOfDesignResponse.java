package com.smart.tailor.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignResponse {
    private UUID partOfDesignID;

    private String partOfDesignName;

    private String imageUrl;

    private String successImageUrl;

    private MaterialResponse material;

    private List<ItemMaskResponse> itemMask;

    private String createDate;

    private String lastModifiedDate;
}
