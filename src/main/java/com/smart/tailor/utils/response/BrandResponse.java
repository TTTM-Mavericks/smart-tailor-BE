package com.smart.tailor.utils.response;

import com.smart.tailor.enums.BrandStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private UUID brandID;
    private UserResponse user;
    private String brandName;
    private BrandStatus brandStatus;
    private Float rating;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private String QR_Payment;
    private String address;
    private String province;
    private String district;
    private String ward;
    private Integer numberOfViolations;
}
