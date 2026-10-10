package com.satyakush.pulseops.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, PulseOpsUserDetailsService.class, JsonAuthenticationEntryPoint.class, JsonAccessDeniedHandler.class})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void authenticatedUserCanReadOwnIdentity() throws Exception {
        mockMvc.perform(get("/api/v1/auth/me").with(httpBasic("operator", "operator")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("operator")))
                .andExpect(jsonPath("$.role", is("OPERATOR")));
    }
}
