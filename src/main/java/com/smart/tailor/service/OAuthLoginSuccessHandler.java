package com.smart.tailor.service;


import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.entities.User;
import com.smart.tailor.enums.Provider;
import com.smart.tailor.utils.request.AuthenticationRequest;
import com.smart.tailor.utils.request.UserRequest;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(OAuthLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
//        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        String oauth2ClientName = oauth2User.getOauth2ClientName();

        logger.info("{}", oauth2ClientName);

        String email = oauth2User.getAttribute("email");
        User user = userService.getUserByEmail(email);
        String targetUrl = APIConstant.AuthenticationAPI.AUTHENTICATION;
        if (user == null) {
            targetUrl += APIConstant.AuthenticationAPI.GOOGLE_REGISTER;
            String fullName = oauth2User.getAttribute("name").toString();
            UserRequest  userRequest = UserRequest.builder()
                    .email(email)
                    .password("12345")
                    .fullName(fullName)
                    .provider(Provider.GOOGLE)
                    .build();

            if(oauth2ClientName.equalsIgnoreCase(Provider.FACEBOOK.name()))
            {
                userRequest.setProvider(Provider.FACEBOOK);
            }

            request.setAttribute("authRequest", userRequest);

        } else {
            targetUrl += APIConstant.AuthenticationAPI.GOOGLE_LOGIN;
            request.setAttribute("authRequest", AuthenticationRequest
                    .builder()
                    .email(email)
                    .provider(Provider.GOOGLE)
                    .build()
            );
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher(targetUrl);
        dispatcher.forward(request, response);
    }
}