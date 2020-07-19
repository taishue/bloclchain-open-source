package com.coinsthai.repository;

import com.coinsthai.model.Coin;

import java.util.List;

/**
 * @author
 */
public interface CoinRepository extends AbstractRepository<Coin> {

    Coin findByName(String name);

    Coin findByFullName(String fullName);

    List<Coin> findAllByOrderByPriorityAsc();
}
