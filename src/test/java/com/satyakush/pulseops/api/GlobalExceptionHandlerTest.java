package com.satyakush.pulseops.api;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void invalidCredentialsUseGenericUnauthorizedResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/auth/login");
        request.setAttribute(RequestIdFilter.ATTRIBUTE, "auth-401");

        ResponseEntity<ApiError> response = handler.handleInvalidCredentials(
                new com.satyakush.pulseops.auth.InvalidCredentialsException(), request);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid username or password", response.getBody().message());
        assertEquals("auth-401", response.getBody().requestId());
    }

    @Test
    void unreadableRequestBodyReturnsSafeBadRequestResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/incidents");
        request.setAttribute(RequestIdFilter.ATTRIBUTE, "body-400");

        ResponseEntity<ApiError> response = handler.handleUnreadableRequest(
                new org.springframework.http.converter.HttpMessageNotReadableException(
                        "sensitive parser details"), request);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Request body contains invalid or unreadable values", response.getBody().message());
        assertEquals("body-400", response.getBody().requestId());
        org.junit.jupiter.api.Assertions.assertFalse(response.getBody().message().contains("sensitive"));
    }

    @Test
    void invalidIncidentTransitionReturnsConflictWithCorrelationId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/incidents/42/status");
        request.setAttribute(RequestIdFilter.ATTRIBUTE, "incident-409");

        ResponseEntity<ApiError> response = handler.handleInvalidIncidentStatusTransition(
                new com.satyakush.pulseops.incident.InvalidIncidentStatusTransitionException(
                        com.satyakush.pulseops.incident.IncidentStatus.OPEN,
                        com.satyakush.pulseops.incident.IncidentStatus.RESOLVED), request);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("incident-409", response.getBody().requestId());
        assertEquals("/api/v1/incidents/42/status", response.getBody().path());
    }

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
}
