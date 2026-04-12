package com.authserver.authserver.redis;

import java.time.Duration;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisCacheService(RedisTemplate<String, Object> redisTemplate,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <T> void set(String key, T value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null)
            return null;

        return objectMapper.convertValue(value, clazz);
    }

    public <T> T getOrDefault(String key, Class<T> clazz, T defaultValue) {
        Object value = redisTemplate.opsForValue().get(key);

        if (value == null) {
            return defaultValue;
        }

        return objectMapper.convertValue(value, clazz);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Set<Object> getDirtyKeys(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public void addToSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
    }

    @Override
    public Set<String> getSetMembers(String key) {
        Set<Object> members = redisTemplate.opsForSet().members(key);
        if (members == null)
            return Set.of();

        return members.stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    @Override
    public void removeFromSet(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    @Override
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }
}
