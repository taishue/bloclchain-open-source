package com.coinsthai.repository;

import com.coinsthai.model.Deal;

import java.util.List;

/**
 * @author
 */
public interface DealRepository extends AbstractRepository<Deal>, DealCustomRepository {

    List<Deal> findBySellId(String sellId);

    List<Deal> findByBuyId(String buyId);

}
