package com.satyakush.pulseops.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class RedisCacheConfigTest {
    @Test
    void createsCacheManagerWithRedisFactory() {
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);
        PulseOpsCacheProperties properties = new PulseOpsCacheProperties();
        properties.setTtl(Duration.ofSeconds(45));

        var manager = new RedisCacheConfig().cacheManager(connectionFactory, new ObjectMapper(), properties);

        assertNotNull(manager.getCache(PulseOpsCacheNames.SERVICES));
    }
}
