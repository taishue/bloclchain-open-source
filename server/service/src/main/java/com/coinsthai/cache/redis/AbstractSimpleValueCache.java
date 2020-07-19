package com.coinsthai.cache.redis;

import com.coinsthai.cache.SimpleValueCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by 
 */
public abstract class AbstractSimpleValueCache<T> implements SimpleValueCache<T> {

    @Autowired
    protected RedisTemplate<String, T> redisTemplate;

    @Override
    public T get(String id) {
        return redisTemplate.opsForValue().get(generateKey(id));
    }

    @Override
    public void set(String id, T value) {
        redisTemplate.opsForValue().set(generateKey(id), value);
    }

    @Override
    public void set(String id, T value, int expires) {
        set(id, value, expires, TimeUnit.MINUTES);
    }

    @Override
    public void set(String id,
                    T value,
                    int expires,
                    TimeUnit timeUnit) {
        String key = generateKey(id);
        redisTemplate.opsForValue().set(key, value);
        expire(key, expires, timeUnit);
    }

    @Override
    public void expire(String id, int expires) {
        expire(id, expires, TimeUnit.MINUTES);
    }

    @Override
    public void expire(String id, int expires, TimeUnit timeUnit) {
        redisTemplate.expire(generateKey(id), expires, timeUnit);
    }

    @Override
    public void delete(String id) {
        redisTemplate.delete(generateKey(id));
    }

    protected abstract String generateKey(String id);
}
