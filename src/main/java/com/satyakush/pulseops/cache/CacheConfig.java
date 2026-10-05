package com.satyakush.pulseops.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Configuration
@EnableCaching
@EnableConfigurationProperties(PulseOpsCacheProperties.class)
public class CacheConfig {
}
