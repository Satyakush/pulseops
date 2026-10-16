package com.satyakush.pulseops.incident;

/**
 * Signals that an incident status change is not allowed by the lifecycle state machine.
 */
public class InvalidIncidentStatusTransitionException extends RuntimeException {
    public InvalidIncidentStatusTransitionException(IncidentStatus current, IncidentStatus requested) {
        super("Cannot transition incident from " + current + " to " + requested);
    }
}
