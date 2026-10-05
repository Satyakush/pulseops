package com.satyakush.pulseops.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class RedisCacheConfigTest {
    @Test
    void createsCacheManagerWithRedisFactory() {
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);

        var manager = new RedisCacheConfig().cacheManager(connectionFactory, new ObjectMapper());

        assertNotNull(manager.getCache(PulseOpsCacheNames.SERVICES));
    }
}
