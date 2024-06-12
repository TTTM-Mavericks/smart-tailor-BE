package com.smart.tailor.service;


import com.smart.tailor.entities.User;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import com.smart.tailor.utils.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public interface AuthenticationService {
    AuthenticationResponse register(UserRequest userRequest) throws Exception;

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    UserResponse updatePassword(UserRequest userRequest);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    String verifyUser(UUID token);

    User forgotPassword(String email);

    User changePassword(String email);

    Boolean verifyPassword(String email, String token);

    Boolean checkVerify(String email);

    Boolean checkVerifyPassword(String email);
}
