package com.coinsthai.cache;

import com.coinsthai.vo.CoinView;

import java.util.List;

/**
 * @author
 */
public interface CoinCache extends SimpleValueCache<CoinView> {

    List<CoinView> listAll();

    void setAll(List<CoinView> list);

}
