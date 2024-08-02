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

    private String brandPriceDeposit;

    private String brandPriceFirstStage;

    private String brandPriceSecondStage;
}
