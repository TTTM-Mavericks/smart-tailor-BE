package com.smart.tailor.config;

import com.smart.tailor.constant.APIConstant;
import com.smart.tailor.enums.ERole;
import com.smart.tailor.service.CustomOAuth2UserService;
import com.smart.tailor.service.OAuthLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final CustomOAuth2UserService oauth2UserService;
    private final OAuthLoginSuccessHandler oauthLoginSuccessHandler;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final RestAccessDenyEntryPoint restAccessDenyEntryPoint;
    private final RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;
    private final LogoutHandler logoutHandler;
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/ws/**"
    };

    private static final String[] authenticatedRole = {
            ERole.CUSTOMER.name(),
            ERole.ADMIN.name(),
            ERole.EMPLOYEE.name(),
            ERole.ACCOUNTANT.name(),
            ERole.MANAGER.name()
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(access -> access.accessDeniedHandler(restAccessDenyEntryPoint))
                .exceptionHandling(access -> access.authenticationEntryPoint(restUnauthorizedEntryPoint))
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/api/v1/auth/update-password").hasAnyRole(authenticatedRole);
                            auth.requestMatchers(WHITE_LIST_URL).permitAll();
                            auth.anyRequest().authenticated();
                })
//                .anyRequest()
//                .authenticated()
//                .and()
//                .oauth2Login()
//                .userInfoEndpoint()
//                .userService(oauth2UserService)
//                .and()
//                .successHandler(oauthLoginSuccessHandler)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCORSFilter(), WebAsyncManagerIntegrationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/log-out")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        return httpSecurity.build();
    }
}
