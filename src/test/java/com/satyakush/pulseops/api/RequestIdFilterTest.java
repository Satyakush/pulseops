package com.satyakush.pulseops.api;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class RequestIdFilterTest {

    private final RequestIdFilter filter = new RequestIdFilter();

    @Test
    void preservesClientRequestId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "client-123");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertEquals("client-123", response.getHeader(RequestIdFilter.HEADER));
        assertEquals("client-123", request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void generatesRequestIdWhenHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertNotNull(response.getHeader(RequestIdFilter.HEADER));
        assertEquals(response.getHeader(RequestIdFilter.HEADER),
                request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void generatedRequestIdIsNonBlank() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, mock(FilterChain.class));
        org.junit.jupiter.api.Assertions.assertFalse(response.getHeader(RequestIdFilter.HEADER).isBlank());
    }

    @Test
    void blankClientRequestIdIsReplaced() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "   ");
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, mock(FilterChain.class));
        org.junit.jupiter.api.Assertions.assertNotNull(response.getHeader(RequestIdFilter.HEADER));
        org.junit.jupiter.api.Assertions.assertFalse(response.getHeader(RequestIdFilter.HEADER).isBlank());
    }
    @Test
    void replacesRequestIdWithControlCharacters() throws Exception {
        for (String invalid : java.util.List.of("line\\nbreak", "tab\\tvalue", "bad/value", "has space")) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(RequestIdFilter.HEADER, invalid);
            MockHttpServletResponse response = new MockHttpServletResponse();

            filter.doFilter(request, response, mock(FilterChain.class));

            String resolved = response.getHeader(RequestIdFilter.HEADER);
            assertNotNull(resolved);
            org.junit.jupiter.api.Assertions.assertNotEquals(invalid, resolved);
            assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
        }
    }

    @Test
    void replacesOverlongRequestIdAndPreservesMaximumLengthSafeId() throws Exception {
        MockHttpServletRequest overlongRequest = new MockHttpServletRequest();
        overlongRequest.addHeader(RequestIdFilter.HEADER, "a".repeat(129));
        MockHttpServletResponse overlongResponse = new MockHttpServletResponse();

        filter.doFilter(overlongRequest, overlongResponse, mock(FilterChain.class));

        assertNotNull(overlongResponse.getHeader(RequestIdFilter.HEADER));
        org.junit.jupiter.api.Assertions.assertNotEquals("a".repeat(129),
                overlongResponse.getHeader(RequestIdFilter.HEADER));

        String boundaryId = "b".repeat(128);
        MockHttpServletRequest boundaryRequest = new MockHttpServletRequest();
        boundaryRequest.addHeader(RequestIdFilter.HEADER, boundaryId);
        MockHttpServletResponse boundaryResponse = new MockHttpServletResponse();

        filter.doFilter(boundaryRequest, boundaryResponse, mock(FilterChain.class));

        assertEquals(boundaryId, boundaryResponse.getHeader(RequestIdFilter.HEADER));
        assertEquals(boundaryId, boundaryRequest.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void continuesFilterChainAfterResolvingRequestId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "ops-check-42");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        org.mockito.Mockito.verify(chain).doFilter(request, response);
    }

}
