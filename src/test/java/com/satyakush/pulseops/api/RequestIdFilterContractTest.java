package com.satyakush.pulseops.api;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequestIdFilterContractTest {
    private final RequestIdFilter filter = new RequestIdFilter();

    @Test
    void generatesUuidWhenHeaderIsMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, mock(FilterChain.class));

        String id = response.getHeader(RequestIdFilter.HEADER);
        assertNotNull(id);
        assertEquals(id, request.getAttribute(RequestIdFilter.ATTRIBUTE));
        assertDoesNotThrow(() -> UUID.fromString(id));
    }

    @Test
    void preservesAllowedCharacters() throws Exception {
        String valid = "trace.ID_42:worker-7";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, valid);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertEquals(valid, response.getHeader(RequestIdFilter.HEADER));
        assertEquals(valid, request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void replacesEmptyAndWhitespaceOnlyIds() throws Exception {
        for (String invalid : new String[]{"", " ", "   ", "\t"}) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(RequestIdFilter.HEADER, invalid);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, mock(FilterChain.class));

            String resolved = response.getHeader(RequestIdFilter.HEADER);
            assertNotNull(resolved);
            assertNotEquals(invalid, resolved);
            assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
        }
    }

    @Test
    void replacesControlAndHeaderDelimiterCharacters() throws Exception {
        for (String invalid : new String[]{"bad/value", "bad,value", "line\nbreak", "tab\tvalue", "semi;colon"}) {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader(RequestIdFilter.HEADER, invalid);
            MockHttpServletResponse response = new MockHttpServletResponse();
            filter.doFilter(request, response, mock(FilterChain.class));

            String resolved = response.getHeader(RequestIdFilter.HEADER);
            assertNotNull(resolved);
            assertNotEquals(invalid, resolved);
            assertEquals(resolved, request.getAttribute(RequestIdFilter.ATTRIBUTE));
        }
    }

    @Test
    void acceptsExactly128SafeCharacters() throws Exception {
        String valid = "x".repeat(128);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, valid);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertEquals(valid, response.getHeader(RequestIdFilter.HEADER));
    }

    @Test
    void replaces129CharacterId() throws Exception {
        String invalid = "x".repeat(129);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(RequestIdFilter.HEADER, invalid);
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, mock(FilterChain.class));

        assertNotEquals(invalid, response.getHeader(RequestIdFilter.HEADER));
        assertEquals(response.getHeader(RequestIdFilter.HEADER),
                request.getAttribute(RequestIdFilter.ATTRIBUTE));
    }

    @Test
    void invokesDownstreamChainExactlyOnce() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
