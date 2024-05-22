package com.smart.tailor.utils.response;


import com.smart.tailor.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer userID;

    private String email;

    private String password;

    private String language;

    private String phoneNumber;

    private Provider provider;

    private String fullName;

    private Boolean userStatus;

    private String address;

    private String province;

    private String district;
}
