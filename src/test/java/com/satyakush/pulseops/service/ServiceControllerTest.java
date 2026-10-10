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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ServiceController.class)
@Import({GlobalExceptionHandler.class, ServiceService.class, InMemoryServiceRepository.class, com.satyakush.pulseops.realtime.PulseOpsEventStream.class})
class ServiceControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test void createServiceReturnsCreatedService() throws Exception {
        mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"payments-api\",\"description\":\"Payment processing API\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.name", is("payments-api")))
                .andExpect(jsonPath("$.status", is("OPERATIONAL")));
    }

    @Test void serviceSummaryCountsStatuses() throws Exception {
        mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"summary-api\",\"description\":\"Summary\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/services/summary")).andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(1))).andExpect(jsonPath("$.operational", is(1)))
                .andExpect(jsonPath("$.degraded", is(0))).andExpect(jsonPath("$.outage", is(0)));
    }

    @Test void listServicesReturnsNamesInStableOrder() throws Exception {
        mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"zulu-api\",\"description\":\"Zulu\"}")).andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"alpha-api\",\"description\":\"Alpha\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/services")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("alpha-api"))).andExpect(jsonPath("$[1].name", is("zulu-api")));
    }

    @Test void listServicesCanFilterByStatus() throws Exception {
        mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"filtered-api\",\"description\":\"Filter me\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/services").param("status", "DEGRADED")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test void missingStatusReturnsBadRequest() throws Exception {
        mockMvc.perform(patch("/api/v1/services/{id}/status", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", is("status: status is required")));
    }

    @Test void blankNameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\" \",\"description\":\"invalid service\"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status", is(400)));
    }

    @Test void missingServiceReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/services/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)));
    }

    @Test void updateServiceStatusReturnsUpdatedService() throws Exception {
        String response = mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"alerts-api\",\"description\":\"Alert delivery API\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = response.replaceAll(".*\\\"id\\\":\\\"([^\\\"]+)\\\".*", "$1");
        mockMvc.perform(patch("/api/v1/services/{id}/status", id).contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"DEGRADED\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status", is("DEGRADED")));
    }

    @Test void deleteServiceReturnsNoContent() throws Exception {
        String response = mockMvc.perform(post("/api/v1/services").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"orders-api\",\"description\":\"Order processing API\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = response.replaceAll(".*\\\"id\\\":\\\"([^\\\"]+)\\\".*", "$1");
        mockMvc.perform(delete("/api/v1/services/{id}", id)).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/services/{id}", id)).andExpect(status().isNotFound());
    }

    @Test void deletingMissingServiceReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/v1/services/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)));
    }
}
