package com.satyakush.pulseops.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@NotBlank(message="username is required") @Size(min=3,max=80,message="username must be 3-80 characters") String username,@NotBlank(message="password is required") @Size(min=8,max=120,message="password must be 8-120 characters") String password) { }
