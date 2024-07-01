package com.smart.tailor.utils.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LaborQuantityResponse {
    private UUID laborQuantityID;

    private Integer laborQuantityMinQuantity;

    private Integer laborQuantityMaxQuantity;

    private Double laborQuantityMinPrice;

    private Double laborQuantityMaxPrice;

    private Boolean status;

    private String createDate;

    private String lastModifiedDate;
}
