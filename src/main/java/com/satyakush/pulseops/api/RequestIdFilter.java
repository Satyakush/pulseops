package com.satyakush.pulseops.api;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestIdFilter extends OncePerRequestFilter {
    public static final String HEADER = "X-Request-Id";
    public static final String ATTRIBUTE = "pulseops.requestId";
    private static final int MAX_REQUEST_ID_LENGTH = 128;
    private static final java.util.regex.Pattern SAFE_REQUEST_ID =
            java.util.regex.Pattern.compile("[A-Za-z0-9._:-]{1,128}");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String requestId = request.getHeader(HEADER);
        if (requestId == null || !SAFE_REQUEST_ID.matcher(requestId).matches()
                || requestId.length() > MAX_REQUEST_ID_LENGTH) {
            requestId = UUID.randomUUID().toString();
        }

        request.setAttribute(ATTRIBUTE, requestId);
        response.setHeader(HEADER, requestId);
        filterChain.doFilter(request, response);
    }
}
