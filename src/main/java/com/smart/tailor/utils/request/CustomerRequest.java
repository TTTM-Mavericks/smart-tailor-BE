package com.smart.tailor.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    private String email;

    private String fullName;

    private String phoneNumber;

    private String imageUrl;

    private Boolean gender;

    private String dateOfBirth;

    private String address;

    private String province;

    private String district;

    private String ward;
}
