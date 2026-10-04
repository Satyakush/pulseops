package com.satyakush.pulseops.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties(PulseOpsSecurityProperties.class)
public class SecurityConfig {
    private final JsonAuthenticationEntryPoint authenticationEntryPoint;
    private final JsonAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(JsonAuthenticationEntryPoint authenticationEntryPoint, JsonAccessDeniedHandler accessDeniedHandler) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                         .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/api/v1/auth/me").hasAnyRole("VIEWER", "OPERATOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/services/**")
                        .hasAnyRole("VIEWER", "OPERATOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/incidents/**")
                        .hasAnyRole("VIEWER", "OPERATOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/services/**")
                        .hasAnyRole("OPERATOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/services/**")
                        .hasAnyRole("OPERATOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/services/**")
                        .hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/incidents/**")
                        .hasAnyRole("OPERATOR", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/incidents/**")
                        .hasAnyRole("OPERATOR", "ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(basic -> {})
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
