package com.smart.tailor.utils.request;

import com.smart.tailor.enums.BrandStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandRequest {
    private String email;
    private String brandName;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String QR_Payment;
    private String address;
    private Float rating;
    private Integer numberOfViolations;
    private BrandStatus brandStatus;
}
