package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserResponseTest { @Test void publicUserResponseNeverContainsPasswordHash(){var response=UserResponse.from(new User(UUID.randomUUID(),"viewer","bcrypt-secret",AuthRole.VIEWER,UserStatus.ACTIVE)); assertEquals("viewer",response.username()); assertFalse(response.toString().contains("bcrypt-secret"));} }
