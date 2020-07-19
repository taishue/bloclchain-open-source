package com.coinsthai.cache;

import com.coinsthai.vo.MarketView;

import java.util.List;

/**
 * @author
 */
public interface MarketCache extends SimpleValueCache<MarketView> {

    List<MarketView> listAll();

    void setAll(List<String> ids);

    /**
     * 获得前端所用的热门市场ID
     *
     * @return
     */
    List<String> listHots();

    void setHots(List<String> marketIds);

    /**
     * 根据基准货币获得
     * @param coinId
     * @return
     */
    List<String> listBaseGrouped(String coinId);

    List<MarketView> listBasedGroupedMarkets(String coinId);

    void setBaseGrouped(String coinId, List<String> marketIds);
}
