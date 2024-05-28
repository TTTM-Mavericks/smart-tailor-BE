package com.smart.tailor.utils.response;


import com.smart.tailor.entities.Roles;
import com.smart.tailor.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private UUID userID;

    private String email;

    private String password;

    private String fullName;

    private String language;

    private String phoneNumber;

    private Provider provider;

    private Boolean userStatus;

    private String roleName;

    private String avatar;
}
