package com.satyakush.pulseops.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PulseOpsUserDetailsService implements UserDetailsService {
    private final PulseOpsSecurityProperties properties;

    public PulseOpsUserDetailsService(PulseOpsSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PulseOpsSecurityProperties.User user = properties.getUsers().stream()
                .filter(candidate -> candidate.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Unknown PulseOps user: " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
