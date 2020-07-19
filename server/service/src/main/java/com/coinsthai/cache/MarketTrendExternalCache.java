package com.coinsthai.cache;

import com.coinsthai.vo.MarketTrendView;

/**
 * 外部交易所的市场行情缓存
 *
 * @author 
 */
public interface MarketTrendExternalCache extends SimpleValueCache<MarketTrendView> {

    boolean hasKeys();
}
