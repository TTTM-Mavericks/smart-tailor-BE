package com.smart.tailor.utils.request;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SampleProductDataRequest {
    private UUID orderID;
    private UUID brandID;
    private String description;
    private String images;
    private String video;
}
