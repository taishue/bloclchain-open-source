package com.coinsthai.service;

import com.coinsthai.vo.MarketView;

import java.util.List;

/**
 * @author
 */
public interface MarketService {

    MarketView create(MarketView view);

    MarketView get(String id);

    List<MarketView> listAll();

    List<MarketView> listActives();

    /**
     * 根据基准币种获得市场列表
     * @param coinId
     * @return
     */
    List<MarketView> listByBaseCoin(String coinId);
}
