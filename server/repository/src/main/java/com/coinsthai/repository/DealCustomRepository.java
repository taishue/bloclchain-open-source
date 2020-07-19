package com.coinsthai.repository;

import com.coinsthai.model.Deal;
import com.coinsthai.model.Kline;
import com.coinsthai.pojo.parametric.DealParametric;
import com.coinsthai.pojo.parametric.DealSimpleParametric;
import org.springframework.data.domain.Page;

import java.util.Date;

/**
 * @author
 */
public interface DealCustomRepository {

    Page<Deal> findSimpleByPage(DealSimpleParametric parametric);

    Page<Deal> findByPage(DealParametric parametric);

    Kline findKline(String marketId, Date beginDate, Date endDate);

    long findFirstPrice(String marketId, Date beginDate, Date endDate);

    long findLastPrice(String marketId, Date beginDate, Date endDate);
}
