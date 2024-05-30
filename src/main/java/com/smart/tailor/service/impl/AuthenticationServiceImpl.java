package com.smart.tailor.service.impl;

import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.Token;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.enums.TokenType;
import com.smart.tailor.service.*;
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
import java.util.HashMap;
import java.util.Map;
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
    private final EmailSenderService emailSenderService;
    private final Map<String, Object> storageObject = new HashMap<>();
    private final Map<String, String> verifyAccount = new HashMap<>();
    private final Map<String, String> expiredTimeLink = new HashMap<>();
    private final Map<String, String> forgotAccount = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Override
    public AuthenticationResponse register(UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Provider provider = userRequest.getProvider() != null ? userRequest.getProvider() : Provider.LOCAL;
        userRequest.setProvider(provider);

        if (provider == Provider.LOCAL) {
            // check if Object exist in HashMap
            UserRequest checkUserRequest = (UserRequest) storageObject.get(userRequest.getEmail());
            if (checkUserRequest != null) {
                String oldToken = verifyAccount.get(userRequest.getEmail());
                LocalDateTime expiredTime = LocalDateTime.parse(expiredTimeLink.get(userRequest.getEmail() + " expiredTime"));
                verifyAccount.remove(oldToken);
                expiredTimeLink.remove(expiredTime);
                storageObject.remove(userRequest.getEmail());
            }

            // Store Object Class to HashMap
            storageObject.put(userRequest.getEmail(), (Object) userRequest);

            String token = UUID.randomUUID().toString();
            verifyAccount.put(userRequest.getEmail(), token);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiredLinkVerify = now.plusMinutes(1);
            expiredTimeLink.put(userRequest.getEmail() + " expiredTime", expiredLinkVerify.toString());

            logger.info("Before Mail Email : {}, token : {}", userRequest.getEmail(), verifyAccount.get(userRequest.getEmail()));

            String verificationUrl = "https://be.mavericks-tttm.studio/api/v1/auth/verify"
                    + "?email=" + userRequest.getEmail()
                    + "&token=" + token;

            String emailText = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "    <title>Account Verification</title>" +
                    "    <style>" +
                    "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                    "        .container { width: 100%; padding: 20px; }" +
                    "        .content { background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    "        .header { font-size: 24px; font-weight: bold; color: #333333; }" +
                    "        .message { font-size: 16px; color: #555555; }" +
                    "        .button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-align: center; text-decoration: none; border-radius: 5px; margin-top: 20px; }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='container'>" +
                    "        <div class='content'>" +
                    "            <div class='header'>Verify Your Account</div>" +
                    "            <div class='message'>Hi " + userRequest.getEmail() + ",</div>" +
                    "            <div class='message'>Thank you for registering. To complete your registration, please verify your email by clicking the button below.</div>" +
                    "            <a href='" + verificationUrl + "' class='button'>Verify Account</a>" +
                    "            <div class='message'>If you did not register for an account, please ignore this email.</div>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            emailSenderService.sendEmail(userRequest.getEmail(), "Account Verification", emailText);
            return new AuthenticationResponse();
        }

        // When Register with Provider Google, Facebook, Github,...
        var user = userService.registerNewUsers(userRequest);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userService.convertToUserResponse(user))
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        try {
            if (authenticationRequest.getProvider() != Provider.GOOGLE) {
                if (authenticationRequest.getPassword().isBlank() || authenticationRequest.getPassword().isEmpty() ||
                        authenticationRequest.getEmail().isEmpty() || authenticationRequest.getEmail().isBlank()) {
                    throw new Exception(MessageConstant.MISSING_ARGUMENT);
                }
                User existedUser = userService.getUserByEmail(authenticationRequest.getEmail());
                if (existedUser == null) {
                    throw new Exception(MessageConstant.USER_NOT_FOUND);
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
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
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
    public Boolean verifyUser(String email, String token) {
        LocalDateTime expiredTime = LocalDateTime.parse(expiredTimeLink.get(email + " expiredTime"));
        LocalDateTime currentTime = LocalDateTime.now();
        String oldToken = verifyAccount.get(email);
        if (currentTime.isAfter(expiredTime)) {
            verifyAccount.remove(oldToken);
            expiredTimeLink.remove(expiredTime);
            storageObject.remove(email);
            return false;
        }
        logger.info(" Get Token From HashMap {}", oldToken);
        if (oldToken.equals(token)) {
            UserRequest userRequest = (UserRequest) storageObject.get(email);
            var user = userService.registerNewUsers(userRequest);
            // Clear storage
            storageObject.remove(email);
            verifyAccount.remove(oldToken);
            expiredTimeLink.remove(expiredTime);
            return true;
        }
        return false;
    }

    @Override
    public void forgotPassword(String email) {
        String token = UUID.randomUUID().toString();
        forgotAccount.put(email, token);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredLinkVerify = now.plusMinutes(1);

        expiredTimeLink.put(email + " expiredTimeForgotPassword", expiredLinkVerify.toString());

        logger.info("Before Mail Email : {}, token : {}", email, forgotAccount.get(email));

        String verificationUrl = "https://be.mavericks-tttm.studio/api/v1/auth/verify-password"
                + "?email=" + email
                + "&token=" + token;

        String emailText = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Account Verification</title>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; }" +
                "        .container { width: 100%; padding: 20px; }" +
                "        .content { background-color: #ffffff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                "        .header { font-size: 24px; font-weight: bold; color: #333333; }" +
                "        .message { font-size: 16px; color: #555555; }" +
                "        .button { display: inline-block; padding: 10px 20px; font-size: 16px; color: #ffffff; background-color: #4CAF50; text-align: center; text-decoration: none; border-radius: 5px; margin-top: 20px; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='content'>" +
                "            <div class='header'>Verify Your Account</div>" +
                "            <div class='message'>Hi " + email + ",</div>" +
                "            <div class='message'>Thank you for registering. To complete your registration, please verify your email by clicking the button below.</div>" +
                "            <a href='" + verificationUrl + "' class='button'>Verify Account</a>" +
                "            <div class='message'>If you did not register for an account, please ignore this email.</div>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
        emailSenderService.sendEmail(email, "Forgot Password", emailText);
    }

    @Override
    public Boolean verifyPassword(String email, String token) {
        LocalDateTime expiredTime = LocalDateTime.parse(expiredTimeLink.get(email + " expiredTimeForgotPassword"));
        LocalDateTime currentTime = LocalDateTime.now();
        String oldToken = forgotAccount.get(email);
        if (currentTime.isAfter(expiredTime)) {
            forgotAccount.remove(oldToken);
            expiredTimeLink.remove(expiredTime);
            return false;
        }
        logger.info(" Get Token From HashMap {}", oldToken);
        if (oldToken.equals(token)) {
            forgotAccount.remove(oldToken);
            expiredTimeLink.remove(expiredTime);
            return true;
        }
        return false;
    }
}
