package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationValidationTest { @Test void registrationRequestRetainsNormalizedInputContract(){var request=new RegisterRequest(" satyam ","strong-pass"); assertEquals(" satyam ",request.username()); assertEquals(11,request.password().length());} 
    @Test void shortUsernameViolatesBeanValidationContract() { var validator = jakarta.validation.Validation.buildDefaultValidatorFactory().getValidator(); var request = new RegisterRequest("ab", "long-password"); org.junit.jupiter.api.Assertions.assertTrue(validator.validate(request).stream().anyMatch(v -> v.getPropertyPath().toString().equals("username"))); }
}
