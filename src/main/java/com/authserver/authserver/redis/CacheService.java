package com.authserver.authserver.redis;

import java.time.Duration;

public interface CacheService {

    <T> void set(String key, T value);

    <T> void set(String key, T value, Duration ttl);

    <T> T get(String key, Class<T> clazz);

    void delete(String key);

    boolean exists(String key);

    Long increment(String key);
}
