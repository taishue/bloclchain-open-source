package com.coinsthai.cache.redis;

import com.coinsthai.cache.MarketTrendCache;
import com.coinsthai.vo.MarketTrendView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class MarketTrendCacheImpl extends AbstractSimpleValueCache<MarketTrendView> implements MarketTrendCache {

    @Override
    protected String generateKey(String id) {
        return RedisKeys.MARKET_TREND + id;
    }
}
