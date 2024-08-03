package com.smart.tailor.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandImageResponse {
    private UUID imageID;
    private String imageUrl;  // Chuỗi Base64 của hình ảnh
    private String imageDescription;
}
