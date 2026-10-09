package com.satyakush.pulseops.auth;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryAuthSessionRepository implements AuthSessionRepository { private final ConcurrentMap<String,AuthSession> sessions=new ConcurrentHashMap<>(); public AuthSession save(AuthSession session){sessions.put(session.token(),session);return session;} public Optional<AuthSession> findByToken(String token){return Optional.ofNullable(sessions.get(token));} public void deleteByToken(String token){sessions.remove(token);} }
