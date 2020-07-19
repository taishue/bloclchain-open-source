package com.coinsthai.controller;

import com.coinsthai.cache.NameCache;
import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author
 */
public abstract class BaseController {

    @Autowired
    protected NameCache nameCache;

    @Autowired
    protected CoinService coinService;

    @Autowired
    protected MarketService marketService;

    protected String translateNameToId(String name) {
        if (name != null && name.length() < 32) {
            if (name.indexOf('-') > 0) {
                name = StringUtils.replace(name, "-", "/");
            }
            if (name.indexOf('_') > 0) {
                name = StringUtils.replace(name, "_", "/");
            }
            name = nameCache.get(name);
        }
        return name;
    }

    protected CoinView getCoin(String coinId, Map<String, CoinView> map) {
        if (map.containsKey(coinId)) {
            return map.get(coinId);
        }
        CoinView coin = coinService.get(coinId);
        map.put(coinId, coin);
        return coin;
    }

    protected MarketView getMarket(String marketId, Map<String, MarketView> map) {
        if (map.containsKey(marketId)) {
            return map.get(marketId);
        }
        MarketView view = marketService.get(marketId);
        map.put(marketId, view);
        return view;
    }

}
