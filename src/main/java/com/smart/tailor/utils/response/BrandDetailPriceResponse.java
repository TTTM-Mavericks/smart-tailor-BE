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
public class BrandDetailPriceResponse {
    private UUID brandID;

    private UUID subOrderID;

    private Double brandPriceDeposit;

    private Double brandPriceFirstStage;

    private Double brandPriceSecondStage;
}
