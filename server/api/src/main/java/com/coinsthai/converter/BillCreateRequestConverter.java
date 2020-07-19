package com.coinsthai.converter;

import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BillCreateApiRequest;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.bill.BillCreateRequest;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BillCreateRequestConverter extends PriceVolumeConverter<BillCreateRequest, BillCreateApiRequest> {

    @Override
    protected String resolveMarketId(BillCreateApiRequest source) {
        return source.getMarketId();
    }

    @Override
    protected void convertSpecials(BillCreateApiRequest source, BillCreateRequest target, MarketView market,
                                   CoinView baseCoin, CoinView targetCoin) {
        target.setPrice(CoinNumberUtils.parseLong(source.getPrice(), baseCoin.getUnit()));
        target.setVolume(CoinNumberUtils.parseLong(source.getVolume(), targetCoin.getUnit()));
    }
}
