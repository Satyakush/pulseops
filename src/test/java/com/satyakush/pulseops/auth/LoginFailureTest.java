package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginFailureTest { @Test void unknownUserUsesCredentialFailure(){var service=new LoginService(new InMemoryUserRepository(),new BCryptPasswordHasher(),new TokenService(),new InMemoryAuthSessionRepository()); assertThrows(InvalidCredentialsException.class,()->service.login(new LoginRequest("missing","password")));} }
