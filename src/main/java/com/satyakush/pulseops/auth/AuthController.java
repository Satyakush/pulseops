package com.satyakush.pulseops.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController { private final RegistrationService registration; private final LoginService login; public AuthController(RegistrationService registration,LoginService login){this.registration=registration;this.login=login;} @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public UserResponse register(@Valid @RequestBody RegisterRequest request){return UserResponse.from(registration.register(request));} @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest request){return login.login(request);} @GetMapping("/me") public CurrentUserResponse me(Authentication authentication){return new CurrentUserResponse(authentication.getName(),AuthRole.valueOf(authentication.getAuthorities().iterator().next().getAuthority().substring(5)));} }
