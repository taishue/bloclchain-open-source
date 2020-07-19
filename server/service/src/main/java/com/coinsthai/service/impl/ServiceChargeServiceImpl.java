package com.coinsthai.service.impl;

import com.coinsthai.model.Market;
import com.coinsthai.model.User;
import com.coinsthai.pojo.common.Constant;
import com.coinsthai.service.ServiceChargeService;
import com.coinsthai.vo.MarketView;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author
 */
@Service
public class ServiceChargeServiceImpl implements ServiceChargeService {

    @Override
    public BigDecimal getBaseBrokerageRate() {
        return new BigDecimal("0.0001");   //暂定万分之一
    }

    @Override
    public BigDecimal getDiscountByMarket(String marketId) {
        return new BigDecimal(1);
    }

    @Override
    public BigDecimal getDiscountByMarket(MarketView market) {
        return new BigDecimal(1);
    }

    @Override
    public BigDecimal getDiscountByMarket(Market market) {
        return new BigDecimal(1);
    }

    @Override
    public BigDecimal getDiscountByUser(String userId) {
        return new BigDecimal(1);
    }

    @Override
    public BigDecimal getDiscountByUser(User user) {
        return new BigDecimal(1);
    }

    @Override
    public BigDecimal getWithdrawRate() {
        return new BigDecimal(1);
    }
}
