package com.smart.tailor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.service.AuthenticationService;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import com.smart.tailor.utils.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.AuthenticationAPI.AUTHENTICATION)
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping(APIConstant.AuthenticationAPI.REGISTER)
    public ObjectNode register(@RequestBody UserRequest userRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Register New Users Successfully");
            respon.set("data", objectMapper.valueToTree(authenticationService.register(userRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.GOOGLE_REGISTER)
    public ObjectNode googleRegister(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserRequest u = (UserRequest) request.getAttribute("authRequest");
        logger.info("{}",  u);
        try {
            return register(
                    u
            );
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.LOGIN)
    public ObjectNode login(@RequestBody AuthenticationRequest authenticationRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            AuthenticationResponse authenticationResponse = authenticationService.login(
                    authenticationRequest);
            respon.put("success", 200);
            respon.put("message", "Login Successfully");
            respon.set("data", objectMapper.valueToTree(authenticationResponse));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.AuthenticationAPI.GOOGLE_LOGIN)
    public ObjectNode googleLogin(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(request);
        try {
            return login(
                    (AuthenticationRequest) request.getAttribute("authRequest")
            );
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(APIConstant.AuthenticationAPI.REFRESH_TOKEN)
    public ObjectNode login(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Login Successfully");
            respon.set("data",
                    objectMapper.valueToTree(authenticationService.refreshToken(request, response)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }
}