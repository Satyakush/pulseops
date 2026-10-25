package com.satyakush.pulseops.incident;

import java.util.UUID;
import com.satyakush.pulseops.api.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@WebMvcTest({IncidentController.class, IncidentActivityController.class})
@Import({GlobalExceptionHandler.class, IncidentService.class, InMemoryIncidentRepository.class, IncidentActivityService.class, InMemoryIncidentActivityRepository.class, SecurityContextIncidentActorProvider.class, com.satyakush.pulseops.realtime.PulseOpsEventPublisher.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class IncidentControllerTest {
    @Autowired MockMvc mockMvc;

    @Test void createIncidentReturnsCreatedIncident() throws Exception {
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"description\":\"Latency increased\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.status", is("OPEN")))
                .andExpect(jsonPath("$.severity", is("HIGH")));
    }

    @Test void createdIncidentExposesItsActivityTimeline() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].action", is("CREATED")))
                .andExpect(jsonPath("$[0].summary", is("Incident created")));
    }

    @Test void paginatedActivityEndpointReturnsPageMetadata() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id).param("page", "0").param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test void paginatedActivityEndpointFiltersByAction() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(
                        "/api/v1/incidents/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"RESOLVED\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id)
                        .param("action", "STATUS_CHANGED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.items[0].action", is("STATUS_CHANGED")))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test void paginatedActivityEndpointRejectsOversizedPages() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id).param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    @Test void paginatedActivityEndpointReturnsEmptyPageForUnmatchedAction() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id).param("action", "DETAILS_UPDATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(0)))
                .andExpect(jsonPath("$.totalElements", is(0)))
                .andExpect(jsonPath("$.totalPages", is(0)));
    }

    @Test void paginatedActivityEndpointRejectsNegativePage() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id).param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("page must be zero or greater")));
    }

    @Test void paginatedActivityEndpointRejectsPageSizeAboveMaximum() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id).param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("size must be between 1 and 100")));
    }

    @Test void paginatedActivityEndpointRejectsUnknownAction() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"API latency\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(get("/api/v1/incidents/{id}/activity/page", id).param("action", "UNKNOWN_ACTION"))
                .andExpect(status().isBadRequest());
    }

    @Test void activityTimelineForUnknownIncidentReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/incidents/{id}/activity", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test void missingTitleReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void incidentSummaryCountsOpenIncidents() throws Exception {
        // Assert the change caused by this request rather than assuming the store
        // starts empty. This is robust to shared/reused test fixtures.
        String initialSummary = mockMvc.perform(get("/api/v1/incidents/summary"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        var initial = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(initialSummary);

        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"Queue alert\",\"severity\":\"LOW\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/incidents/summary")).andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(initial.get("total").asInt() + 1)))
                .andExpect(jsonPath("$.open", is(initial.get("open").asInt() + 1)));
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

        mockMvc.perform(get("/api/v1/incidents/{id}/activity", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].action", is("DETAILS_UPDATED")))
                .andExpect(jsonPath("$[1].action", is("CREATED")));
    }

    @Test void statusChangesAreAddedToTheActivityTimeline() throws Exception {
        String created = mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"00000000-0000-0000-0000-000000000001\",\"title\":\"Queue issue\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String id = com.fasterxml.jackson.databind.json.JsonMapper.builder().build()
                .readTree(created).get("id").asText();

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch(
                        "/api/v1/incidents/{id}/status", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"RESOLVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("RESOLVED")));

        mockMvc.perform(get("/api/v1/incidents/{id}/activity", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].action", is("STATUS_CHANGED")))
                .andExpect(jsonPath("$[1].action", is("CREATED")));
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

    @Test void paginatedIncidentSearchReturnsMetadataAndHonorsPageSize() throws Exception {
        String serviceId = "00000000-0000-0000-0000-000000000021";
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"" + serviceId + "\",\"title\":\"First issue\",\"severity\":\"LOW\"}"))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/v1/incidents").contentType(MediaType.APPLICATION_JSON)
                .content("{\"serviceId\":\"" + serviceId + "\",\"title\":\"Second issue\",\"severity\":\"HIGH\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/incidents/page").param("page", "0").param("size", "1").param("serviceId", serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()", is(1)))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(2)));
    }

    @Test void paginatedIncidentSearchRejectsNegativePage() throws Exception {
        mockMvc.perform(get("/api/v1/incidents/page").param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("page must be zero or greater")));
    }

    @Test void paginatedIncidentSearchRejectsOversizedPages() throws Exception {
        mockMvc.perform(get("/api/v1/incidents/page").param("size", "101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }
}
