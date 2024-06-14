package com.smart.tailor.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final RestAccessDenyEntryPoint restAccessDenyEntryPoint;
    private final RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;
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
            "ADMIN", "MANAGER", "ACCOUNTANT", "CUSTOMER", "EMPLOYEE", "BRAND"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(access -> {
                    access.accessDeniedHandler(restAccessDenyEntryPoint);
                    access.authenticationEntryPoint(restUnauthorizedEntryPoint);
                })
                .authorizeHttpRequests(auth -> {
//                            auth.requestMatchers("/api/v1/auth/update-password").hasAnyRole(authenticatedRole);
//                            auth.requestMatchers("/api/v1/notification/**").hasAnyRole("ADMIN", "MANAGER");
                            auth.requestMatchers(WHITE_LIST_URL).permitAll();
                            auth.anyRequest().permitAll();
                        }
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCORSFilter(), WebAsyncManagerIntegrationFilter.class);
        return httpSecurity.build();
    }
}
