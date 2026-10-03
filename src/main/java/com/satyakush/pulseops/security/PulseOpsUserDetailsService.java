package com.satyakush.pulseops.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PulseOpsUserDetailsService implements UserDetailsService {
    private final Map<String, PulseOpsUser> users = Map.of(
            "viewer", new PulseOpsUser("viewer", "{noop}viewer", PulseOpsRole.VIEWER),
            "operator", new PulseOpsUser("operator", "{noop}operator", PulseOpsRole.OPERATOR),
            "admin", new PulseOpsUser("admin", "{noop}admin", PulseOpsRole.ADMIN)
    );

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PulseOpsUser user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown PulseOps user: " + username);
        }

        return User.withUsername(user.username())
                .password(user.password())
                .roles(user.role().name())
                .build();
    }
}
