package com.coinsthai.repository;

import com.coinsthai.model.Bill;
import com.coinsthai.pojo.parametric.BillParametric;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author
 */
public interface BillCustomRepository {

    int BILL_COUNT_TO_DEAL = 10;

    Page<Bill> findByPage(BillParametric parametric);

    List<Bill> findMatchBuys(String marketId, long price);

    List<Bill> findMatchSells(String marketId, long price);

    List<Bill> findLowestPendingSells(String marketId);

    List<Bill> findHighestPendingBuys(String marketId);
}
