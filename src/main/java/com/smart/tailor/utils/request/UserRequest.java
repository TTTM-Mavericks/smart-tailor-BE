package com.smart.tailor.utils.request;


import com.smart.tailor.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String email;

    private String password;

    private String language;

    private String phoneNumber;

    private String fullName;

    private String address;

    private String province;

    private Provider provider;

    private String district;
}
