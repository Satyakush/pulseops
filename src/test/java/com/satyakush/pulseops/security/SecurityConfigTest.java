package com.satyakush.pulseops.security;

import org.junit.jupiter.api.Test;
import com.satyakush.pulseops.service.ServiceController;
import com.satyakush.pulseops.service.ServiceService;
import com.satyakush.pulseops.service.InMemoryServiceRepository;
import com.satyakush.pulseops.realtime.PulseOpsEventPublisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("legacy-basic-auth")
@WebMvcTest(ServiceController.class)
@Import({SecurityConfig.class, PulseOpsUserDetailsService.class, JsonAuthenticationEntryPoint.class, JsonAccessDeniedHandler.class, ServiceService.class, InMemoryServiceRepository.class, PulseOpsEventPublisher.class})
class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void protectedApiRejectsAnonymousRequests() throws Exception {
        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void viewerCanReadServices() throws Exception {
        mockMvc.perform(get("/api/v1/services").with(httpBasic("viewer", "viewer")))
                .andExpect(status().isOk());
    }
}
