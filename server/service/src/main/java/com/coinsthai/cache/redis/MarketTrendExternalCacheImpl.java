package com.coinsthai.cache.redis;

import com.coinsthai.cache.MarketTrendExternalCache;
import com.coinsthai.vo.MarketTrendView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class MarketTrendExternalCacheImpl extends AbstractSimpleValueCache<MarketTrendView>
        implements MarketTrendExternalCache {

    @Override
    protected String generateKey(String id) {
        return RedisKeys.MARKET_TREND_EXTERNAL + id;
    }

    @Override
    public MarketTrendView get(String id) {
        MarketTrendView view = super.get(id);

        // 要保证外部市场数据有效
        if (view == null) {
            view = new MarketTrendView();
            view.setVolume(0);
            view.setChange(0);
            view.setLast(0);
            view.setHigh(0);
            view.setLow(0);
        }

        return view;
    }

    @Override
    public boolean hasKeys() {
        return redisTemplate.keys(RedisKeys.MARKET_TREND_EXTERNAL + "*").size() > 0;
    }
}
