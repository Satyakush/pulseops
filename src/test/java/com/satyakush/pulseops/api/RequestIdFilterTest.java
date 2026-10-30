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
    void replacesRequestIdContainingWhitespaceOrUnsupportedCharacters() throws Exception {
        for (String invalid : java.util.List.of("has space", "line\nbreak", "bad/value", "tab\tvalue")) {
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
    void replacesRequestIdLongerThan128Characters() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "a".repeat(129));
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        String resolved = response.getHeader(RequestIdFilter.HEADER);
        assertNotNull(resolved);
        org.junit.jupiter.api.Assertions.assertNotEquals("a".repeat(129), resolved);
        assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void preservesSafeRequestIdAtMaximumLength() throws Exception {
        String valid = "a".repeat(128);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, valid);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertEquals(valid, response.getHeader(RequestIdFilter.HEADER));
        assertEquals(valid, request.getAttribute(RequestIdFilter.ATTRIBUTE));
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

    @Test
    void replacesDuplicateRequestIdHeaders() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "first-id");
        request.addHeader(RequestIdFilter.HEADER, "second-id");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        String resolved = response.getHeader(RequestIdFilter.HEADER);
        assertNotNull(resolved);
        org.junit.jupiter.api.Assertions.assertNotEquals("first-id", resolved);
        org.junit.jupiter.api.Assertions.assertNotEquals("second-id", resolved);
        assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }


    @Test
    void replacesEmptyClientRequestId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        String resolved = response.getHeader(RequestIdFilter.HEADER);
        assertNotNull(resolved);
        org.junit.jupiter.api.Assertions.assertFalse(resolved.isBlank());
        org.junit.jupiter.api.Assertions.assertNotEquals("", resolved);
        assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void replacesNonAsciiClientRequestId() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, "café-123");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        String resolved = response.getHeader(RequestIdFilter.HEADER);
        assertNotNull(resolved);
        org.junit.jupiter.api.Assertions.assertNotEquals("café-123", resolved);
        org.junit.jupiter.api.Assertions.assertTrue(resolved.matches("[A-Za-z0-9._:-]{1,128}"));
        assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }


}
