package com.satyakush.pulseops.service;

public class DuplicateServiceNameException extends RuntimeException {

    public DuplicateServiceNameException(String name) {
        super("Service already exists: " + name);
    }
}
