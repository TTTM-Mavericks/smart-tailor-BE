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
public class SystemImageResponse {
    private UUID imageID;
    private String imageName;
    private String imageURL;
    private Boolean imageStatus;
    private String imageType;
    private Boolean isPremium;
}
