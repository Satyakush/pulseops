package com.satyakush.pulseops.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class SessionAuthenticationFilter extends OncePerRequestFilter { private final TokenExtractor extractor; private final SessionService sessions; public SessionAuthenticationFilter(TokenExtractor extractor,SessionService sessions){this.extractor=extractor;this.sessions=sessions;} @Override protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain) throws ServletException,IOException {String token=extractor.extract(request); if(token!=null){try{AuthenticatedUser user=sessions.authenticate(token); var auth=new UsernamePasswordAuthenticationToken(user.username(),null,java.util.stream.Stream.concat(java.util.stream.Stream.of(new SimpleGrantedAuthority("ROLE_"+user.role().name())),RolePermissions.forRole(user.role()).stream().map(permission->new SimpleGrantedAuthority(permission.name()))).toList()); SecurityContextHolder.getContext().setAuthentication(auth);}catch(SessionNotFoundException ex){SecurityContextHolder.clearContext();response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid or expired bearer token");return;}} chain.doFilter(request,response);} }
