package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.constant.MessageConstant;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.service.AuthenticationService;
import com.smart.tailor.service.UserService;
import com.smart.tailor.utils.Utilities;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping(APIConstant.AuthenticationAPI.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;

    @GetMapping(APIConstant.AuthenticationAPI.VERIFY)
    public ResponseEntity<ObjectNode> verifyAccount(@RequestParam("email") String email, @RequestParam("token") String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = authenticationService.verifyUser(email, token);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_VERIFIED_SUCCESSFULLY);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", MessageConstant.INVALID_VERIFICATION_TOKEN);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.VERIFY_PASSWORD)
    public ResponseEntity<ObjectNode> verifyPassword(@RequestParam("email") String email, @RequestParam("token") String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = authenticationService.verifyPassword(email, token);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_VERIFIED_SUCCESSFULLY);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", MessageConstant.INVALID_VERIFICATION_TOKEN);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN VERIFY UPDATE PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.CHECK_VERIFY + "/{email}")
    public ResponseEntity<ObjectNode> checkVerify(@PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = authenticationService.checkVerify(email);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_IS_VERIFIED);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", MessageConstant.ACCOUNT_NOT_VERIFIED);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN CHECK VERIFY PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.CHECK_VERIFY_PASSWORD + "/{email}")
    public ResponseEntity<ObjectNode> checkVerifyPassword(@PathVariable("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            boolean isVerified = authenticationService.checkVerifyPassword(email);
            if (isVerified) {
                respon.put("status", 200);
                respon.put("message", MessageConstant.ACCOUNT_IS_VERIFIED);
                return ResponseEntity.ok(respon);
            } else {
                respon.put("status", 401);
                respon.put("message", MessageConstant.ACCOUNT_NOT_VERIFIED);
                return ResponseEntity.ok(respon);
            }
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN CHECK VERIFY ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.REGISTER)
    public ResponseEntity<ObjectNode> register(@RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            // Check if enough argument?
            if (userRequest == null || userRequest.getEmail() == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            String email = userRequest.getEmail();
            String password = userRequest.getPassword();

            // Check email is valid?
            if (!Utilities.isValidEmail(email)) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.INVALID_EMAIL);
                return ResponseEntity.ok(respon);
            }

            // Check password is valid? Only check when it's not google registration
            if (userRequest.getProvider() != Provider.GOOGLE) {
                if (!Utilities.isValidPassword(password)) {
                    respon.put("status", 400);
                    respon.put("message", MessageConstant.INVALID_PASSWORD);
                    return ResponseEntity.ok(respon);
                }
            }

            // Check email is duplicated?
            if (userService.getUserByEmail(userRequest.getEmail()) != null) {
                respon.put("status", 409);
                respon.put("message", MessageConstant.DUPLICATE_REGISTERED_EMAIL);
                return ResponseEntity.ok(respon);
            }

            AuthenticationResponse authenticationResponse = authenticationService.register(userRequest);
            if (authenticationResponse == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.REGISTER_NEW_USER_FAILED);
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.REGISTER_NEW_USER_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN REGISTER ACCOUNT. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.FORGOT_PASSWORD)
    public ResponseEntity<ObjectNode> forgotPassword(@RequestParam("email") String email) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            authenticationService.forgotPassword(email);
            respon.put("status", 200);
            respon.put("message", MessageConstant.SEND_MAIL_FOR_UPDATE_PASSWORD_SUCCESSFULLY);
            return ResponseEntity.ok(respon);

        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN SEND MAIL FORGOT PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.UPDATE_PASWORD)
    public ResponseEntity<ObjectNode> updatePassword(@RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            if (userRequest == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.BAD_REQUEST);
                return ResponseEntity.ok(respon);
            }

            if (userRequest.getEmail() == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }

            String email = userRequest.getEmail();
            if (!Utilities.isValidEmail(email)) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.INVALID_EMAIL);
                return ResponseEntity.ok(respon);
            }

            if (userRequest.getPassword() == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.MISSING_ARGUMENT);
                return ResponseEntity.ok(respon);
            }
            String password = userRequest.getPassword();
            if (!Utilities.isValidPassword(password)) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.INVALID_PASSWORD);
                return ResponseEntity.ok(respon);
            }

            UserResponse userResponse = authenticationService.updatePassword(userRequest);
            if (userResponse == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.UPDATE_PASSWORD_FAILED);
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.UPDATE_PASSWORD_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(userResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN UPDATE PASSWORD. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.LOGIN)
    public ResponseEntity<ObjectNode> login(@RequestBody AuthenticationRequest authenticationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
            if (authenticationResponse == null) {
                respon.put("status", 401);
                respon.put("message", MessageConstant.INVALID_EMAIL_OR_PASSWORD);
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.LOGIN_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN LOGIN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.GOOGLE_LOGIN)
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> payload) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            String token = payload.get("authRequest");
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance()).setAudience(Collections.singletonList(clientId)).build();
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload googlePayload = idToken.getPayload();
                String email = googlePayload.getEmail();
                String fullName = (String) googlePayload.get("name");
                String imageUrl = (String) googlePayload.get("picture");
                String language = (String) googlePayload.get("language");
                User user = userService.getUserByEmail(email);
                if (user == null) {

                    ResponseEntity<ObjectNode> response = register(UserRequest.builder().email(email).password(clientId).provider(Provider.GOOGLE).language(language).fullName(fullName).build());

                    if (response.getStatusCode().is2xxSuccessful()) {
                        UserResponse userResponse = objectMapper.treeToValue(response.getBody().get("data").get("user"), UserResponse.class);
                        userResponse.setImageUrl(imageUrl);

                        AuthenticationResponse authenticationResponse = objectMapper.treeToValue(response.getBody().get("data"), AuthenticationResponse.class);
                        authenticationResponse.setUser(userResponse);
                        respon.put("status", 200);
                        respon.put("message", MessageConstant.REGISTER_NEW_USER_SUCCESSFULLY);
                        respon.set("data", objectMapper.valueToTree(authenticationResponse));
                        return ResponseEntity.ok(respon);
                    }
                    return response;
                }
                return login(AuthenticationRequest.builder().provider(Provider.GOOGLE).email(email).password(clientId).build());
            }
            respon.put("status", 401);
            respon.put("message", MessageConstant.INVALID_VERIFICATION_TOKEN);
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN GOOGLE LOGIN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.REFRESH_TOKEN)
    public ResponseEntity<ObjectNode> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        try {
            AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request, response);
            if (authenticationResponse == null) {
                respon.put("status", 400);
                respon.put("message", MessageConstant.REFRESH_TOKEN_FAILED);
                return ResponseEntity.ok(respon);
            }
            respon.put("status", 200);
            respon.put("message", MessageConstant.REFRESH_TOKEN_SUCCESSFULLY);
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return ResponseEntity.ok(respon);
        } catch (Exception ex) {
            respon.put("status", -1);
            respon.put("message", MessageConstant.INTERNAL_SERVER_ERROR);
            logger.error("ERROR IN REFRESH TOKEN. ERROR MESSAGE: {}", ex.getMessage());
            return ResponseEntity.ok(respon);
        }
    }
}