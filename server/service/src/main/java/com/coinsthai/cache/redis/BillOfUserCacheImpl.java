package com.coinsthai.cache.redis;

import com.coinsthai.cache.BillOfUserCache;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.vo.bill.BillView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author
 */
@Component
public class BillOfUserCacheImpl implements BillOfUserCache {

    @Autowired
    protected RedisTemplate<String, List<BillView>> redisTemplate;

    @Override
    public void set(String userId, String marketId, boolean finished, List<BillView> bills) {
        String key = generateKey(userId, marketId, finished);
        redisTemplate.opsForValue().set(key, bills, 5, TimeUnit.MINUTES);
    }

    @Override
    public List<BillView> get(String userId, String marketId, boolean finished) {
        String key = generateKey(userId, marketId, finished);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean cached(String userId, String marketId, boolean finished) {
        String key = generateKey(userId, marketId, finished);
        return redisTemplate.hasKey(key);
    }

    @Override
    public void delete(String userId, String marketId, boolean finished) {
        String key = generateKey(userId, marketId, finished);
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String userId, String marketId) {
        delete(userId, marketId, true);
        delete(userId, marketId, false);
    }

    private String generateKey(String userId, String marketId, boolean finished) {
        String type = finished ? "-1-" : "-0-";
        return RedisKeys.BILLS + userId + type + marketId;
    }
}
