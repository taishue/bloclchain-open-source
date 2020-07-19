package com.coinsthai.converter;

import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BillSimpleApiView;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.bill.BillSimpleView;

/**
 * @author
 */
public class BillSimpleFromViewConverter extends PriceVolumeConverter<BillSimpleApiView, BillSimpleView> {

    private String marketId;

    public BillSimpleFromViewConverter(String marketId, MarketService marketService, CoinService coinService) {
        this.marketId = marketId;
        this.marketService = marketService;
        this.coinService = coinService;
    }

    @Override
    protected String resolveMarketId(BillSimpleView source) {
        return marketId;
    }

    @Override
    protected void convertSpecials(BillSimpleView source, BillSimpleApiView target, MarketView market,
                                   CoinView baseCoin, CoinView targetCoin) {
        target.setPrice(CoinNumberUtils.formatDoubleVolume(source.getPrice(), baseCoin.getUnit()));
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getVolume(), targetCoin.getUnit()));
    }
}
