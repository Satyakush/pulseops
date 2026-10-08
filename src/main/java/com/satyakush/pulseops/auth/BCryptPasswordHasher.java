package com.satyakush.pulseops.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher { private final BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(); public String hash(String rawPassword){return encoder.encode(rawPassword);} public boolean matches(String rawPassword,String hash){return encoder.matches(rawPassword,hash);} }
