package com.satyakush.pulseops.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenExtractor { public String extract(HttpServletRequest request){String header=request.getHeader("Authorization"); if(header==null||!header.startsWith("Bearer ")) return null; String token=header.substring(7).trim(); return token.isEmpty()?null:token;} }
