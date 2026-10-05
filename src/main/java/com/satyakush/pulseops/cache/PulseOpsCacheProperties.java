package com.satyakush.pulseops.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "pulseops.cache")
public class PulseOpsCacheProperties {
    private Duration ttl = Duration.ofSeconds(60);

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(Duration ttl) {
        this.ttl = ttl;
    }
}
