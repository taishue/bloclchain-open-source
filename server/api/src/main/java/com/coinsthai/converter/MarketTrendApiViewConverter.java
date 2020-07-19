package com.coinsthai.converter;

import com.coinsthai.pojo.common.Constant;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketTrendApiView;
import com.coinsthai.vo.MarketTrendView;
import com.coinsthai.vo.MarketView;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class MarketTrendApiViewConverter extends PriceVolumeConverter<MarketTrendApiView, MarketTrendView> {

    @Override
    protected String resolveMarketId(MarketTrendView source) {
        return source.getId();
    }

    @Override
    protected void convertSpecials(MarketTrendView source, MarketTrendApiView target, MarketView market,
                                   CoinView baseCoin, CoinView targetCoin) {
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getVolume(), targetCoin.getUnit()));
        target.setFirst(CoinNumberUtils.formatDoubleVolume(source.getFirst(), baseCoin.getUnit()));
        target.setLast(CoinNumberUtils.formatDoubleVolume(source.getLast(), baseCoin.getUnit()));
        target.setHigh(CoinNumberUtils.formatDoubleVolume(source.getHigh(), baseCoin.getUnit()));
        target.setLow(CoinNumberUtils.formatDoubleVolume(source.getLow(), baseCoin.getUnit()));
        target.setChange(CoinNumberUtils.formatDouble(source.getChange() / (double) Constant.CHANGE_RATE_UNIT,
                                                      CoinNumberUtils.toDecimalLength(Constant.CHANGE_RATE_UNIT)));
    }
}
