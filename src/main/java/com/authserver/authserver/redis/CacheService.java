package com.authserver.authserver.redis;

import java.time.Duration;
import java.util.Set;

public interface CacheService {

    <T> void set(String key, T value);

    <T> void set(String key, T value, Duration ttl);

    <T> T get(String key, Class<T> clazz);

    void delete(String key);

    boolean exists(String key);

    Long increment(String key);

    void addToSet(String key, String value);

    Set<String> getSetMembers(String key);

    void removeFromSet(String key, String value);
}
