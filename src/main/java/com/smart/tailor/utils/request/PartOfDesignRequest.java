package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartOfDesignRequest {
    private String partOfDesignName;

    private String imageUrl;

    private String successImageUrl;

    private List<ItemMaskRequest> itemMaskList;
}
