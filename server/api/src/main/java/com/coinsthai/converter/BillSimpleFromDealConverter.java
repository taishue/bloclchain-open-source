package com.coinsthai.converter;

import com.coinsthai.model.Deal;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BillSimpleApiView;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BillSimpleFromDealConverter extends PriceVolumeConverter<BillSimpleApiView, Deal> {

    @Override
    protected String resolveMarketId(Deal source) {
        return source.getMarket().getId();
    }

    @Override
    protected void convertSpecials(Deal source, BillSimpleApiView target, MarketView market, CoinView baseCoin,
                                   CoinView targetCoin) {
        target.setPrice(CoinNumberUtils.formatDoubleVolume(source.getPrice(), baseCoin.getUnit()));
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getVolume(), targetCoin.getUnit()));
    }

}
