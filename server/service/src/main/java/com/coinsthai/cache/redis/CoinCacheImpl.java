package com.coinsthai.cache.redis;

import com.coinsthai.cache.CoinCache;
import com.coinsthai.cache.NameCache;
import com.coinsthai.vo.CoinView;
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
public class CoinCacheImpl extends AbstractSimpleValueCache<CoinView> implements CoinCache {

    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;

    @Autowired
    private NameCache nameCache;

    @Override
    protected String generateKey(String id) {
        return RedisKeys.COIN + id;
    }

    @Override
    public List<CoinView> listAll() {
        List<CoinView> list = new ArrayList<>();

        List<String> marketIds = stringRedisTemplate.opsForList().range(RedisKeys.COINS_ALL, 0, -1);
        if (!marketIds.isEmpty()) {
            marketIds.forEach(id -> list.add(get(id)));
        }

        return list;
    }

    @Override
    public void setAll(List<CoinView> list) {
        stringRedisTemplate.delete(RedisKeys.COINS_ALL);

        List<String> ids = new ArrayList<>(list.size());
        list.forEach(market -> {
            ids.add(market.getId());
            set(market.getId(), market);
        });
        stringRedisTemplate.opsForList().rightPushAll(RedisKeys.COINS_ALL, ids);
    }

    @Override
    public void set(String id, CoinView value) {
        super.set(id, value);
        nameCache.set(value.getName(), id);
    }

    @Override
    public void set(String id, CoinView value, int expires, TimeUnit timeUnit) {
        super.set(id, value, expires, timeUnit);
        nameCache.set(value.getName(), id, expires, timeUnit);
    }
}
