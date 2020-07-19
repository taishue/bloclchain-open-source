package com.coinsthai.converter;

import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.DealApiView;
import com.coinsthai.vo.MarketView;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class DealApiViewConverter extends PriceVolumeConverter<DealApiView, Deal> {

    @Override
    protected String resolveMarketId(Deal source) {
        return source.getMarket().getId();
    }

    @Override
    protected void convertSpecials(Deal source, DealApiView target, MarketView market, CoinView baseCoin,
                                   CoinView targetCoin) {
        target.setPrice(CoinNumberUtils.formatDoubleVolume(source.getPrice(), baseCoin.getUnit()));
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getVolume(), targetCoin.getUnit()));
        target.setMarketId(market.getId());
        target.setMarketName(market.getName());

        Bill sell = source.getSell();
        target.setSellId(sell.getId());
        target.setSellerId(sell.getUser().getId());
        target.setSellBrokerage(CoinNumberUtils.formatDoubleVolume(source.getSellBrokerage(), baseCoin.getUnit()));

        Bill buy = source.getBuy();
        target.setBuyerId(buy.getId());
        target.setBuyerId(buy.getUser().getId());
        target.setBuyBrokerage(CoinNumberUtils.formatDoubleVolume(source.getBuyBrokerage(), targetCoin.getUnit()));
    }
}
