package com.satyakush.pulseops.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    SessionAuthenticationFilter sessionAuthenticationFilter(TokenExtractor extractor, SessionService sessions) {
        return new SessionAuthenticationFilter(extractor, sessions);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, SessionAuthenticationFilter sessionFilter) throws Exception {
        return http.csrf(csrf -> csrf.disable())
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login", "/actuator/health").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/services/**").hasAuthority("SERVICE_READ")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/services/**").hasAuthority("SERVICE_WRITE")
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/services/**").hasAuthority("SERVICE_WRITE")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/services/**").hasAuthority("SERVICE_WRITE")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/incidents/**").hasAuthority("INCIDENT_READ")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/incidents/**").hasAuthority("INCIDENT_WRITE")
                .requestMatchers(org.springframework.http.HttpMethod.PATCH, "/api/v1/incidents/**").hasAuthority("INCIDENT_WRITE")
                .anyRequest().authenticated())
            .addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
