package com.satyakush.pulseops.service;

import java.util.UUID;

import com.satyakush.pulseops.api.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceController.class)
@Import({GlobalExceptionHandler.class, ServiceService.class, InMemoryServiceRepository.class})
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createServiceReturnsCreatedService() throws Exception {
        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"payments-api","description":"Payment processing API"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("payments-api")))
                .andExpect(jsonPath("$.status", is("OPERATIONAL")));
    }

    @Test
    void blankNameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":" ","description":"invalid service"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test
    void missingServiceReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/services/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }
}
