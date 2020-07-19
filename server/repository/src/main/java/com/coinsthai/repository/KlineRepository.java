package com.coinsthai.repository;

import com.coinsthai.model.Kline;
import com.coinsthai.pojo.intenum.KlineType;

import java.util.List;

/**
 * @author 
 */
public interface KlineRepository extends AbstractRepository<Kline> {

    Kline findByMarketIdAndTimestampAndType(String marketId, long timestamp, KlineType type);

    Kline findTop1ByMarketIdAndTypeAndTimestampLessThanOrderByTimestampDesc(String marketId, KlineType type,
                                                                            long timestamp);

    List<Kline> findTop200ByMarketIdAndTypeOrderByTimestampDesc(String marketId, KlineType type);

    long countByMarketIdAndType(String marketId, KlineType type);
}
