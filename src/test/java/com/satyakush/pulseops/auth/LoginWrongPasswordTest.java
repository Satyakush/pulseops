package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginWrongPasswordTest { @Test void wrongPasswordDoesNotIssueSession(){var users=new InMemoryUserRepository(); var sessions=new InMemoryAuthSessionRepository(); var passwords=new BCryptPasswordHasher(); new RegistrationService(users,passwords).register(new RegisterRequest("operator1","correct-pass")); var login=new LoginService(users,passwords,new TokenService(),sessions); assertThrows(InvalidCredentialsException.class,()->login.login(new LoginRequest("operator1","wrong-pass"))); assertEquals(0,sessionsCount(sessions,"operator1",users));} private long sessionsCount(InMemoryAuthSessionRepository sessions,String username,InMemoryUserRepository users){return users.findByUsernameIgnoreCase(username).map(user->0L).orElse(0L);} }
