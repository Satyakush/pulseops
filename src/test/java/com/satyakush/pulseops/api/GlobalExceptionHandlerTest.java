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

        ApiError error = handler.handleUnexpectedException(
                new IllegalStateException("boom"), request);

        assertEquals(500, error.status());
        assertEquals(null, error.requestId());
    }
}
