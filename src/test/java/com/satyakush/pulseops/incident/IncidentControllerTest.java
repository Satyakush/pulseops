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

    @Test void invalidSeverityReturnsBadRequest() throws Exception { mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON).content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"Bad severity\",\"severity\":\"URGENT\"}")).andExpect(status().isBadRequest()); }

    @Test void activeFilterExcludesResolvedIncidents() throws Exception {
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON).content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"Active issue\",\"severity\":\"HIGH\"}")).andExpect(status().isCreated());
        mockMvc.perform(get("/api/v1/incidents").param("filter", "ACTIVE")).andExpect(status().isOk()).andExpect(jsonPath("$[*].title", org.hamcrest.Matchers.hasItem("Active issue")));
    }
    @Test void updateIncidentDetailsPreservesLifecycleStatus() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"Queue alert\",\"severity\":\"LOW\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/incidents/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Queue backlog\",\"description\":\"Worker queue is growing\",\"severity\":\"CRITICAL\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Queue backlog")))
                .andExpect(jsonPath("$.severity", is("CRITICAL")))
                .andExpect(jsonPath("$.status", is("OPEN")));
    }

    @Test void updateIncidentDetailsRejectsBlankTitle() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/incidents/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\" \",\"description\":\"bad\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test void filtersIncidentsBySeverityAndService() throws Exception {
        String serviceId = "00000000-0000-0000-0000-000000000011";
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"" + serviceId + "\",\"title\":\"Low priority\",\"severity\":\"LOW\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"" + serviceId + "\",\"title\":\"Critical priority\",\"severity\":\"CRITICAL\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/incidents").param("severity", "CRITICAL").param("serviceId", serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].title", is("Critical priority")));
    }
}


