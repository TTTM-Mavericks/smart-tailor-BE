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
public class AuthenticationRequest {
    private String email;
    private String password;
    private Provider provider;
}
