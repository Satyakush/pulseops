package com.satyakush.pulseops.incident;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityContextIncidentActorProviderTest {
    private final SecurityContextIncidentActorProvider provider = new SecurityContextIncidentActorProvider();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void fallsBackToSystemWhenNoAuthenticationExists() {
        SecurityContextHolder.clearContext();

        assertThat(provider.currentActor()).isEqualTo("system");
    }

    @Test
    void usesAuthenticatedPrincipalName() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("on-call-operator", "ignored", List.of()));

        assertThat(provider.currentActor()).isEqualTo("on-call-operator");
    }

    @Test
    void doesNotRecordAnonymousUserAsAnActor() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("anonymousUser", "ignored"));

        assertThat(provider.currentActor()).isEqualTo("system");
    }
}
