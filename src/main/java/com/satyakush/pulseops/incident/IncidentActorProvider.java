package com.satyakush.pulseops.incident;

/** Supplies the principal name for an audit entry without coupling storage to HTTP. */
@FunctionalInterface
public interface IncidentActorProvider {
    String currentActor();
}
