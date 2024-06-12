package com.smart.tailor.utils.response;

import com.smart.tailor.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String email;

    private String fullName;

    private String phoneNumber;

    private Boolean userStatus;

    private String roleName;

    private String imageUrl;

    private Boolean gender;

    private Date dateOfBirth;

    private String address;

    private String province;

    private String district;

    private String ward;
}
