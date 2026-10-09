package com.satyakush.pulseops.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController { private final RegistrationService registration; private final LoginService login; private final TokenExtractor tokens; private final SessionService sessions; public AuthController(RegistrationService registration,LoginService login,TokenExtractor tokens,SessionService sessions){this.registration=registration;this.login=login;this.tokens=tokens;this.sessions=sessions;} @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED) public UserResponse register(@Valid @RequestBody RegisterRequest request){return UserResponse.from(registration.register(request));} @PostMapping("/login") public AuthResponse login(@Valid @RequestBody LoginRequest request){return login.login(request);} @GetMapping("/me") public CurrentUserResponse me(Authentication authentication){return new CurrentUserResponse(authentication.getName(),authentication.getAuthorities().stream().filter(a->a.getAuthority().startsWith("ROLE_")).map(a->AuthRole.valueOf(a.getAuthority().substring(5))).findFirst().orElseThrow());} @PostMapping("/logout") @ResponseStatus(HttpStatus.NO_CONTENT) public void logout(jakarta.servlet.http.HttpServletRequest request){String token=tokens.extract(request); if(token!=null) sessions.revoke(token); } }
