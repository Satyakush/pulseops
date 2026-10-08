package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationValidationTest { @Test void registrationRequestRetainsNormalizedInputContract(){var request=new RegisterRequest(" satyam ","strong-pass"); assertEquals(" satyam ",request.username()); assertEquals(11,request.password().length());} }
