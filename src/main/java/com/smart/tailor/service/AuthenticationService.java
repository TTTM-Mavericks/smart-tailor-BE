package com.smart.tailor.service;


import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public interface AuthenticationService {
    AuthenticationResponse register(UserRequest userRequest);

    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
