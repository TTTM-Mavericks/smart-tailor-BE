package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.entities.Image;
import com.smart.tailor.entities.User;
import com.smart.tailor.entities.UsingImage;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.service.AuthenticationService;
import com.smart.tailor.service.ImageService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.service.UsingImageService;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import com.smart.tailor.utils.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(APIConstant.AuthenticationAPI.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UsingImageService usingImageService;
    private final ImageService imageService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @PostMapping(APIConstant.AuthenticationAPI.REGISTER)
    public ResponseEntity<ObjectNode> register(@RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            AuthenticationResponse authenticationResponse = authenticationService.register(userRequest);
            if (authenticationResponse == null) {
                respon.put("error", 200);
                respon.put("message", "Error: Failed to register new user.");
                return ResponseEntity.ok(respon);
            }
            respon.put("success", 200);
            respon.put("message", "Register New Users Successfully");
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.LOGIN)
    public ResponseEntity<ObjectNode> login(@RequestBody AuthenticationRequest authenticationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
            if (authenticationResponse == null) {
                respon.put("error", 200);
                respon.put("message", "Error: Login failed.");
                return ResponseEntity.ok(respon);
            }
            respon.put("success", 200);
            respon.put("message", "Login Successfully");
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.GOOGLE_LOGIN)
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        String token = payload.get("authRequest");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload googlePayload = idToken.getPayload();
                String email = googlePayload.getEmail();
                String fullName = (String) googlePayload.get("name");
                String imageUrl = (String) googlePayload.get("picture");
                String language = (String) googlePayload.get("locale");
                logger.info(fullName + " " + imageUrl);
                User user = userService.getUserByEmail(email);
                if (user == null) {
                    Image img = Image.builder()
                            .imageUrl(imageUrl)
                            .name(fullName + " AVATAR")
                            .build();

                    ResponseEntity<ObjectNode> response = register(
                            UserRequest
                                    .builder()
                                    .email(email)
                                    .password(clientId)
                                    .provider(Provider.GOOGLE)
                                    .language(language)
                                    .fullName(fullName)
                                    .build()
                    );

                    if (response.getStatusCode().is2xxSuccessful() && response.getBody().get("success") != null) {
                        Image i = imageService.saveImage(img);
                        ObjectMapper objectMapper = new ObjectMapper();
                        UserResponse userResponse = objectMapper.treeToValue(response.getBody().get("data").get("user"), UserResponse.class);
                        usingImageService.saveUsingImage(
                                UsingImage
                                        .builder()
                                        .image(i)
                                        .type("AVATAR")
                                        .relationID(userResponse.getUserID())
                                        .build()
                        );

                        userResponse.setAvatar(
                                imageService.getImageUrl(
                                        usingImageService.getImage("AVATAR", userResponse.getUserID())
                                )
                        );

                        AuthenticationResponse authenticationResponse = objectMapper.treeToValue(
                                response.getBody().get("data"), AuthenticationResponse.class
                        );
                        authenticationResponse.setUser(userResponse);
                        ObjectNode respon = objectMapper.createObjectNode();
                        respon.put("success", 200);
                        respon.put("message", "Register New Users Successfully");
                        respon.set("data", objectMapper.valueToTree(authenticationResponse));
                        return ResponseEntity.ok(respon);
                    }
                    return response;
                }
                return login(
                        AuthenticationRequest
                                .builder()
                                .provider(Provider.GOOGLE)
                                .email(email)
                                .password(clientId)
                                .build()
                );
            }
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
    }

    @PostMapping(APIConstant.AuthenticationAPI.REFRESH_TOKEN)
    public ResponseEntity<ObjectNode> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request, response);
            if (authenticationResponse == null) {
                respon.put("error", 200);
                respon.put("message", "Error: Failed to refresh token.");
                return ResponseEntity.ok(respon);
            }
            respon.put("success", 200);
            respon.put("message", "Login Successfully");
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respon);
        }
    }
}