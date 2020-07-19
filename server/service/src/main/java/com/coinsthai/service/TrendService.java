package com.coinsthai.service;

import com.coinsthai.model.Deal;
import com.coinsthai.model.Kline;
import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.vo.KlineView;
import com.coinsthai.vo.MarketTrendView;
import com.coinsthai.vo.MarketView;

import java.util.List;

/**
 * @author
 */
public interface TrendService {

    /**
     * 创建K线的点
     * 比如15分钟K线，若传入2018-06-06 12:16:12.123，则计算[12:00:00.000, 12:15:00.00)的区间
     *
     * @param marketId 市场ID
     * @param end      结束时间的毫秒数
     * @param type
     * @return
     */
    Kline createKline(String marketId, long end, KlineType type);

    /**
     * 根据最新成交，更新缓存中的最新K线点
     *
     * @param marketId
     * @param deal
     */
    void updateKlines(String marketId, Deal deal);

    /**
     * 根据已有交易初始化K线数据库
     * 如果指定类型及市场已有K线数据，即不处理
     * 否则（最多）生成最近200条记录
     *
     * @param marketId
     * @param type
     */
    void initializeKlines(String marketId, KlineType type);

    List<KlineView> listKlines(String marketId, KlineType type);

    /**
     * 重新计算市场趋势
     *
     * @param market
     * @return
     */
    MarketTrendView updateTrend(MarketView market);

    /**
     * 根据新的交易更新市场趋势
     *
     * @param marketId
     * @param deal
     * @return
     */
    MarketTrendView updateTrend(String marketId, Deal deal);

    MarketTrendView getTrend(String marketId);

    List<MarketTrendView> listHotTrends();

    /**
     * 根据基准币种获得相应市场的动态列表
     *
     * @param coinId
     * @return
     */
    List<MarketTrendView> listByBaseCoin(String coinId);

    /**
     * 根据外部API更新外部市场动态
     */
    void updateExternalTrend();
}
