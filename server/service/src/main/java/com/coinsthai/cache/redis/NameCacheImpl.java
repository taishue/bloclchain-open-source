package com.coinsthai.cache.redis;

import com.coinsthai.cache.NameCache;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class NameCacheImpl extends AbstractSimpleValueCache<String> implements NameCache {

    @Override
    protected String generateKey(String id) {
        return RedisKeys.NAME + id.toUpperCase();
    }
}
