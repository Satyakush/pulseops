package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import static org.junit.jupiter.api.Assertions.*;

class TokenExtractorTest { @Test void extractsBearerToken(){var request=new MockHttpServletRequest(); request.addHeader("Authorization","Bearer abc123"); assertEquals("abc123",new TokenExtractor().extract(request));} @Test void rejectsOtherAuthorizationSchemes(){var request=new MockHttpServletRequest(); request.addHeader("Authorization","Basic abc123"); assertNull(new TokenExtractor().extract(request));} }
