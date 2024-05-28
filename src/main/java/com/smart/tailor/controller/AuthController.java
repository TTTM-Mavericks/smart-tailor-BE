package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.entities.Image;
import com.smart.tailor.entities.UsingImage;
import com.smart.tailor.service.AuthenticationService;
import com.smart.tailor.service.UsingImageService;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.AuthenticationAPI.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UsingImageService usingImageService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    @GetMapping(APIConstant.AuthenticationAPI.GOOGLE_REGISTER)
    public ResponseEntity<ObjectNode> googleRegister(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode respon = objectMapper.createObjectNode();
        UserRequest u = (UserRequest) request.getAttribute("authRequest");
        Image img = (Image) request.getAttribute("img");
        logger.info("{}", u);
        logger.info("{}", img);
        try {
            AuthenticationResponse authenticationResponse = authenticationService.register(u);
            if (authenticationResponse == null) {
                respon.put("error", 200);
                respon.put("message", "Error: Failed to register new user.");
                return ResponseEntity.ok(respon);
            }
            usingImageService.saveUsingImage(
                    UsingImage.builder()
                            .image(img)
                            .type("AVATAR")
                            .relationID(authenticationResponse.getUser().getUserID())
                            .build()
            );
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

    @GetMapping(APIConstant.AuthenticationAPI.GOOGLE_LOGIN)
    public ResponseEntity<ObjectNode> googleLogin(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(request);
        try {
            return login((AuthenticationRequest) request.getAttribute("authRequest"));
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respon);
        }
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