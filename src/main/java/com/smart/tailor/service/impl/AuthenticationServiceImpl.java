package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Token;
import com.smart.tailor.entities.User;
import com.smart.tailor.entities.VerificationToken;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.TokenType;
import com.smart.tailor.enums.TypeOfVerification;
import com.smart.tailor.enums.UserStatus;
import com.smart.tailor.service.*;
import com.smart.tailor.utils.Utilities;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import com.smart.tailor.utils.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final VerificationTokenService verificationTokenService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public AuthenticationResponse register(UserRequest userRequest) throws Exception {
        try {
            // Store Persist User in DB whether Provider is Local or Google
            // Login with Provider Local Status is false otherwise true
            userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
            Provider provider = userRequest.getProvider() != null ? userRequest.getProvider() : Provider.LOCAL;
            userRequest.setProvider(provider);
            var user = userService.registerNewUsers(userRequest);

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse
                    .builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .user(userService.convertToUserResponse(user))
                    .build();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            if (authenticationRequest.getProvider() != Provider.GOOGLE) {
                if (authenticationRequest.getPassword().isBlank() || authenticationRequest.getPassword().isEmpty() ||
                        authenticationRequest.getEmail().isEmpty() || authenticationRequest.getEmail().isBlank()) {
                    throw new Exception("MISSING ARGUMENT");
                }

                if(!Utilities.isValidEmail(authenticationRequest.getEmail())){
                    throw new Exception("EMAIL IS INVALID");
                }

                User existedUser = userService.getUserByEmail(authenticationRequest.getEmail());
                if (existedUser == null) {
                    throw new Exception("USER IS NOT EXISTED");
                }

                if (!existedUser.getUserStatus().equals(UserStatus.ACTIVE)) {
                    throw new Exception("USER ARE NOT ALLOW TO ENTER!");
                }

                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticationRequest.getEmail(),
                                authenticationRequest.getPassword()
                        )
                );
            }
            var user = userService.getUserByEmail(authenticationRequest.getEmail());
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse
                    .builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .user(userService.convertToUserResponse(user))
                    .build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public UserResponse updatePassword(UserRequest userRequest) {
        User user = userService.getUserByEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userService.saveOrUpdateUser(user);
        return userService.convertToUserResponse(user);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenService.saveToken(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenService.findAllValidTokenByUser(user.getUserID());
        if (validUserTokens.isEmpty())
            return;
        tokenService.revokeAllUserTokens(validUserTokens);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new AuthenticationResponse();
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = null;
            try {
                user = userService.getUserByEmail(userEmail);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (jwtService.isValidToken(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse
                        .builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user(userService.convertToUserResponse(user))
                        .build();

                return authResponse;
            }
        }
        return null;
    }

    @Override
    public String verifyUser(UUID token) {
        // Check token is existed or not
        var verificationTokenOptional = verificationTokenService.findByToken(token);
        if (verificationTokenOptional.isEmpty()) {
            return MessageConstant.INVALID_VERIFICATION_TOKEN;
        }

        var verificationToken = verificationTokenOptional.get();
        User user = verificationToken.getUser();
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Check ExpirationDateTime with CurrentDateTime
        if (currentDateTime.isAfter(verificationToken.getExpirationDateTime())) {
            return MessageConstant.TOKEN_ALREADY_EXPIRED;
        }

        // Change Status User
        if (verificationToken.getTypeOfVerification().equals(TypeOfVerification.VERIFY_ACCOUNT)) {
            userService.updateStatusAccount(user.getEmail(), UserStatus.ACTIVE);
        }
        verificationTokenService.enableVerificationToken(verificationToken);
        return MessageConstant.TOKEN_IS_VALID;
    }

    @Override
    public User forgotPassword(String email) {
        var user = userService.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        var token = UUID.randomUUID();
        verificationTokenService.saveUserVerificationToken(user, token, TypeOfVerification.FORGOT_PASSWORD);
        return user;
    }

    @Override
    public User changePassword(String email) {
        var user = userService.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        var token = UUID.randomUUID();
        verificationTokenService.saveUserVerificationToken(user, token, TypeOfVerification.CHANGE_PASSWORD);
        return user;
    }

    @Override
    public Boolean checkVerifyAccount(String email) {
        try {
            var registerUser = userService.getUserByEmail(email);
            VerificationToken verificationToken = verificationTokenService.findVerificationTokenByUserEmail(email);
            return registerUser != null && registerUser.getUserStatus().equals(UserStatus.ACTIVE) && verificationToken.isEnabled();
        } catch (Exception ex) {
            logger.error("ERROR IN AuthenticationServiceImpl - checkVerify: {}", ex.getMessage());
        }
        return false;
    }

    @Override
    public Boolean checkVerifyPassword(String email, TypeOfVerification typeOfVerification) {
        try {
            VerificationToken verificationToken = verificationTokenService.findVerificationTokenByUserEmail(email);
            return email != null && verificationToken != null && verificationToken.getTypeOfVerification().equals(typeOfVerification) && verificationToken.isEnabled();

        } catch (Exception ex) {
            logger.error("ERROR IN AuthenticationServiceImpl - checkVerifyPassword: {}", ex.getMessage());
        }
        return false;
    }
}
