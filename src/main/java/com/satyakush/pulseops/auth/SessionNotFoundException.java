package com.satyakush.pulseops.auth;

public class SessionNotFoundException extends RuntimeException { public SessionNotFoundException(){super("Authentication session is invalid or expired");} }
