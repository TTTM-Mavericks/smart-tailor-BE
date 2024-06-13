package com.smart.tailor.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smart.tailor.utils.request.ItemMaskRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignResponse {
    private String partOfDesignName;

    private String imageUrl;

    private String successImageUrl;

    private List<ItemMaskRequest> itemMaskRequestList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;
}
