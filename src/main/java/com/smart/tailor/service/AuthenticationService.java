package com.smart.tailor.service;


import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import com.smart.tailor.utils.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface AuthenticationService {
    AuthenticationResponse register(UserRequest userRequest) throws Exception;

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    UserResponse updatePassword(UserRequest userRequest);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    Boolean verifyUser(String email, String token) throws Exception;

    void forgotPassword(String email);

    Boolean verifyPassword(String email, String token);

    Boolean checkVerify(String email);

    Boolean checkVerifyPassword(String email);
}
