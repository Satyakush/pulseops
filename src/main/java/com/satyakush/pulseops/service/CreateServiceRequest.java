package com.satyakush.pulseops.service;

import com.satyakush.pulseops.api.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateServiceRequest(
        @NotBlank(message = ValidationMessages.REQUIRED_NAME)
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description
) {
}
