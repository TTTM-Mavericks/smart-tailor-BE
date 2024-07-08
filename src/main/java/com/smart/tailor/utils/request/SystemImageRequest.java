package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemImageRequest {
    private String imageName;
    private String imageURL;
    private String imageType;
    private Boolean isPremium;
}
