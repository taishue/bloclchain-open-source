package com.coinsthai.converter;

import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.KlineView;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
@Component
public class KlineDataConverter extends AbstractPojoConverter<double[], KlineView> {

    @Autowired
    private MarketService marketService;

    @Autowired
    private CoinService coinService;

    @Override
    public void toPojo(KlineView source, double[] target) {
        MarketView market = marketService.get(source.getMarketId());
        CoinView baseCoin = coinService.get(market.getBaseId());
        CoinView targetCoin = coinService.get(market.getTargetId());
        toPojo(source, target, baseCoin, targetCoin);
    }

    @Override
    public List<double[]> toList(List<KlineView> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return super.toList(sourceList);
        }

        MarketView market = marketService.get(sourceList.get(0).getMarketId());
        CoinView baseCoin = coinService.get(market.getBaseId());
        CoinView targetCoin = coinService.get(market.getTargetId());

        List<double[]> targetList = new ArrayList<>(sourceList.size());
        sourceList.forEach(source -> {
            double[] target = createEmptyTarget(source);
            toPojo(source, target, baseCoin, targetCoin);
            targetList.add(target);
        });

        return targetList;
    }

    @Override
    protected double[] createEmptyTarget(KlineView source) {
        return new double[6];
    }

    private void toPojo(KlineView source, double[] target, CoinView baseCoin, CoinView targetCoin) {
        target[0] = source.getTimestamp();

        // 法币保留两位小数，其他保留六位小数
        int baseUnit = baseCoin.getUnit();
        int decimalLength = CoinNumberUtils.toDecimalLength(baseUnit);
        if (!baseCoin.isBase()) {
            decimalLength -= 2;
        }
        target[1] = CoinNumberUtils.formatDouble(source.getFirst() / ((double) baseUnit), decimalLength);
        target[2] = CoinNumberUtils.formatDouble(source.getHigh() / ((double) baseUnit), decimalLength);
        target[3] = CoinNumberUtils.formatDouble(source.getLow() / ((double) baseUnit), decimalLength);
        target[4] = CoinNumberUtils.formatDouble(source.getLast() / ((double) baseUnit), decimalLength);

        // 数量
        target[5] = CoinNumberUtils.formatDoubleVolume(source.getVolume(), targetCoin.getUnit());
    }

}
