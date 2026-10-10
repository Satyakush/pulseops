package com.satyakush.pulseops.incident;

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

@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(IncidentController.class)
@Import({GlobalExceptionHandler.class, IncidentService.class, InMemoryIncidentRepository.class, com.satyakush.pulseops.realtime.PulseOpsEventPublisher.class})
class IncidentControllerTest {
    @Autowired MockMvc mockMvc;

    @Test void createIncidentReturnsCreatedIncident() throws Exception {
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"description\":\"Latency increased\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.status", is("OPEN")))
                .andExpect(jsonPath("$.severity", is("HIGH")));
    }

    @Test void missingTitleReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void incidentSummaryCountsOpenIncidents() throws Exception {
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"Queue alert\",\"severity\":\"LOW\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/incidents/summary")).andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(1))).andExpect(jsonPath("$.open", is(1)));
    }

    @Test void missingIncidentReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/incidents/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status", is(404)));
    }
}
