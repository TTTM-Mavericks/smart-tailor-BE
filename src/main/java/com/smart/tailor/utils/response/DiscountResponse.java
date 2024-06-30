package com.smart.tailor.utils.response;

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
public class DiscountResponse {
    private UUID discountID;
    private String discountName;
    private Double discountPercent;
    private Integer quantity;
    private LocalDateTime startDateTime;
    private LocalDateTime expiredDateTime;
}
