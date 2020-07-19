package com.coinsthai.service;

import com.coinsthai.model.Market;
import com.coinsthai.model.User;
import com.coinsthai.vo.MarketView;

import java.math.BigDecimal;

/**
 * 手续费服务
 *
 * @author
 */
public interface ServiceChargeService {

    /**
     * 获得基准交易手续费
     *
     * @return
     */
    BigDecimal getBaseBrokerageRate();

    /**
     * 获得指定市场的手续费折扣
     *
     * @param marketId
     * @return
     */
    BigDecimal getDiscountByMarket(String marketId);

    /**
     * 获得指定市场的手续费折扣
     *
     * @param market
     * @return
     */
    BigDecimal getDiscountByMarket(MarketView market);

    /**
     * 获得指定市场的手续费折扣
     *
     * @param market
     * @return
     */
    BigDecimal getDiscountByMarket(Market market);

    /**
     * 获得指定用户的手续费折扣
     *
     * @param userId
     * @return
     */
    BigDecimal getDiscountByUser(String userId);

    /**
     * 获得指定用户的手续费折扣
     *
     * @param user
     * @return
     */
    BigDecimal getDiscountByUser(User user);

    // TODO 优惠活动折扣

    // TODO 平台币折扣

    /**
     * 获得提现手续费率
     *
     * @return
     */
    BigDecimal getWithdrawRate();

}
