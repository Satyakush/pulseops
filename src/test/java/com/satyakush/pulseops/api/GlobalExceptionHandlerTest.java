package com.satyakush.pulseops.api;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void unexpectedErrorsPreserveRequestCorrelationId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/services");
        request.setAttribute(RequestIdFilter.ATTRIBUTE, "req-500");

        ResponseEntity<ApiError> response = handler.handleUnexpectedException(
                new IllegalStateException("boom"), request);
        ApiError error = response.getBody();
        assertEquals(500, response.getStatusCode().value());

        assertEquals(500, error.status());
        assertEquals("req-500", error.requestId());
        assertEquals("/api/v1/services", error.path());
    }

    @Test
    void errorsWithoutCorrelationContextRemainValid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        org.mockito.Mockito.when(request.getRequestURI()).thenReturn("/api/v1/health");

        ResponseEntity<ApiError> response = handler.handleUnexpectedException(
                new IllegalStateException("boom"), request);
        ApiError error = response.getBody();

        assertEquals(500, response.getStatusCode().value());
        assertEquals(null, error.requestId());
    }

    @Test
    void unexpectedErrorsReturnGenericClientMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/services");
        ResponseEntity<ApiError> response = handler.handleUnexpectedException(new IllegalStateException("database password leaked"), request);
        assertEquals("An unexpected error occurred", response.getBody().message());
        org.junit.jupiter.api.Assertions.assertFalse(response.getBody().message().contains("database password"));
    }
    @Test
    void invalidQueryParameterReturnsBadRequestWithSafeMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/incidents/123/activity/page");
        request.setAttribute(RequestIdFilter.ATTRIBUTE, "req-400");
        org.springframework.web.method.annotation.MethodArgumentTypeMismatchException exception =
                new org.springframework.web.method.annotation.MethodArgumentTypeMismatchException(
                        "UNKNOWN", com.satyakush.pulseops.incident.IncidentActivityAction.class,
                        "action", null, new IllegalArgumentException("invalid enum"));

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(exception, request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(400, response.getBody().status());
        assertEquals("Invalid value for parameter 'action'", response.getBody().message());
        assertEquals("/api/v1/incidents/123/activity/page", response.getBody().path());
        assertEquals("req-400", response.getBody().requestId());
    }
    @Test
    void requestIdFilterPreservesClientSuppliedCorrelationId() throws Exception {
        RequestIdFilter filter = new RequestIdFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "trace-client-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilterInternal(request, response, (req, res) -> { });

        assertEquals("trace-client-123", request.getAttribute(RequestIdFilter.ATTRIBUTE));
        assertEquals("trace-client-123", response.getHeader(RequestIdFilter.HEADER));
    }

    @Test
    void requestIdFilterGeneratesUuidWhenHeaderIsMissingOrBlank() throws Exception {
        RequestIdFilter filter = new RequestIdFilter();

        for (String suppliedId : new String[] { null, "   " }) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            if (suppliedId != null) {
                request.addHeader(RequestIdFilter.HEADER, suppliedId);
            }
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilterInternal(request, response, (req, res) -> { });

            String generatedId = request.getAttribute(RequestIdFilter.ATTRIBUTE).toString();
            assertDoesNotThrow(() -> UUID.fromString(generatedId));
            assertEquals(generatedId, response.getHeader(RequestIdFilter.HEADER));
        }
    }
}
