package com.coinsthai.cache.redis;

import com.coinsthai.cache.KlineCache;
import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.vo.KlineView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * K线缓存
 * 按timestamp asc排序，right push
 *
 * @author
 */
@Component
public class KlineCacheImpl implements KlineCache {

    @Autowired
    private RedisTemplate<String, KlineView> redisTemplate;

    @Override
    public void add(String marketId, KlineType type, KlineView view) {
        String key = generateKey(marketId, type);
        KlineView latest = redisTemplate.opsForList().rightPop(key);
        if (latest == null) {
            redisTemplate.opsForList().rightPush(key, view);
            return;
        }

        if (latest.getTimestamp() > view.getTimestamp()) {
            //队列中最新一个比要增加的还新，存入原来的并放弃要增加的
            redisTemplate.opsForList().rightPush(key, latest);
            return;
        }
        else if (latest.getTimestamp() < view.getTimestamp()) {
            // 队列中最新的一个比要增加的旧，那么两个都加入队列中
            redisTemplate.opsForList().rightPush(key, latest);
            redisTemplate.opsForList().rightPush(key, view);

            // 如果超过敲定的最大长度即修剪掉最旧的
            int size = redisTemplate.opsForList().size(key).intValue();
            if (size > MAX_LENGTH) {
                redisTemplate.opsForList().trim(key, size - MAX_LENGTH, -1);
            }
        }
        else {
            // 覆盖队列中最新的
            redisTemplate.opsForList().rightPush(key, view);
        }

    }

    @Override
    public void set(String marketId, KlineType type, List<KlineView> views) {
        String key = generateKey(marketId, type);
        redisTemplate.delete(key);

        redisTemplate.opsForList().rightPushAll(key, views);
    }

    @Override
    public List<KlineView> getAll(String marketId, KlineType type) {
        return redisTemplate.opsForList().range(generateKey(marketId, type), 0, -1);
    }

    @Override
    public KlineView getLatest(String marketId, KlineType type) {
        return redisTemplate.opsForList().index(generateKey(marketId, type), -1);
    }

    protected String generateKey(String id, KlineType type) {
        return RedisKeys.KLINE + id + ":" + String.valueOf(type.getNumber());
    }

}
