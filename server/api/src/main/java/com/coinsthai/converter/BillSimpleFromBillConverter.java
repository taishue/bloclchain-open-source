package com.coinsthai.converter;

import com.coinsthai.model.Bill;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BillSimpleApiView;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class BillSimpleFromBillConverter extends PriceVolumeConverter<BillSimpleApiView, Bill> {

    @Override
    protected String resolveMarketId(Bill source) {
        return source.getMarket().getId();
    }

    @Override
    protected void convertSpecials(Bill source, BillSimpleApiView target, MarketView market, CoinView baseCoin,
                                   CoinView targetCoin) {
        target.setPrice(CoinNumberUtils.formatDoubleVolume(source.getPrice(), baseCoin.getUnit()));
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getRemainVolume(), targetCoin.getUnit()));
    }
}
