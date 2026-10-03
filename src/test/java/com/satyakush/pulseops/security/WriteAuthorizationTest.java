package com.satyakush.pulseops.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({SecurityConfig.class, PulseOpsUserDetailsService.class})
class WriteAuthorizationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void readOnlyUserCannotCreateService() throws Exception {
        mockMvc.perform(post("/api/v1/services")
                        .with(httpBasic("viewer", "viewer"))
                        .contentType("application/json")
                        .content("{\"name\":\"payments-api\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void operatorCannotDeleteService() throws Exception {
        mockMvc.perform(delete("/api/v1/services/00000000-0000-0000-0000-000000000001")
                        .with(httpBasic("operator", "operator")))
                .andExpect(status().isForbidden());
    }
}
