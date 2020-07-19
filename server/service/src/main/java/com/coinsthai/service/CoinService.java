package com.coinsthai.service;

import com.coinsthai.vo.CoinView;

import java.util.List;

/**
 * @author
 */
public interface CoinService {

    CoinView create(CoinView view);

    CoinView get(String id);

    CoinView getByName(String name);

    List<CoinView> listAll();

    List<CoinView> listActives();
}
