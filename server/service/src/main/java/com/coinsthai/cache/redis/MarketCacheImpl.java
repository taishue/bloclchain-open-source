package com.coinsthai.cache.redis;

import com.coinsthai.cache.MarketCache;
import com.coinsthai.cache.NameCache;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Component
public class MarketCacheImpl extends AbstractSimpleValueCache<MarketView> implements MarketCache {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private NameCache nameCache;

    @Override
    public List<MarketView> listAll() {
        List<MarketView> list = new ArrayList<>();

        List<String> marketIds = stringRedisTemplate.opsForList().range(RedisKeys.MARKETS_ALL, 0, -1);
        if (!marketIds.isEmpty()) {
            List<String> keys = new ArrayList<>(marketIds.size());
            marketIds.forEach(id -> keys.add(generateKey(id)));
            list.addAll(redisTemplate.opsForValue().multiGet(keys));
        }

        return list;
    }

    @Override
    public void setAll(List<String> list) {
        stringRedisTemplate.delete(RedisKeys.MARKETS_ALL);
        stringRedisTemplate.opsForList().rightPushAll(RedisKeys.MARKETS_ALL, list);
    }

    @Override
    public List<String> listHots() {
        return stringRedisTemplate.opsForList().range(RedisKeys.MARKETS_HOTS, 0, -1);
    }

    @Override
    public void setHots(List<String> marketIds) {
        stringRedisTemplate.delete(RedisKeys.MARKETS_HOTS);
        stringRedisTemplate.opsForList().rightPushAll(RedisKeys.MARKETS_HOTS, marketIds);
    }

    @Override
    public List<String> listBaseGrouped(String coinId) {
        return stringRedisTemplate.opsForList().range(generateGroupedKey(coinId), 0, -1);
    }

    @Override
    public List<MarketView> listBasedGroupedMarkets(String coinId) {
        List<MarketView> list = new ArrayList<>();

        List<String> marketIds = listBaseGrouped(coinId);
        if (!marketIds.isEmpty()) {
            List<String> keys = new ArrayList<>(marketIds.size());
            marketIds.forEach(id -> keys.add(generateKey(id)));
            list.addAll(redisTemplate.opsForValue().multiGet(keys));
        }

        return list;
    }

    @Override
    public void setBaseGrouped(String coinId, List<String> marketIds) {
        String key = generateGroupedKey(coinId);
        stringRedisTemplate.delete(key);
        stringRedisTemplate.opsForList().rightPushAll(key, marketIds);
    }

    @Override
    public void set(String id, MarketView value) {
        super.set(id, value);
        nameCache.set(value.getName(), id);
    }

    @Override
    public void set(String id, MarketView value, int expires, TimeUnit timeUnit) {
        super.set(id, value, expires, timeUnit);
        nameCache.set(value.getName(), id, expires, timeUnit);
    }

    @Override
    protected String generateKey(String id) {
        return RedisKeys.MARKET + id;
    }

    private String generateGroupedKey(String coinId) {
        return RedisKeys.MARKETS_BASE_GROUPED + coinId;
    }

}
