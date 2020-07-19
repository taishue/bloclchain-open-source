package com.coinsthai.converter;

import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 */
public abstract class PriceVolumeConverter<T, S> extends BeanCopyPojoConverter<T, S> {

    @Autowired
    protected MarketService marketService;

    @Autowired
    protected CoinService coinService;

    @Override
    protected void afterBeanCopy(S source, T target) {
        super.afterBeanCopy(source, target);

        MarketView market = marketService.get(resolveMarketId(source));
        if (market == null) {
            return;
        }

        CoinView baseCoin = coinService.get(market.getBaseId());
        CoinView targetCoin = coinService.get(market.getTargetId());
        toPojo(source, target, market, baseCoin, targetCoin);
        convertSpecials(source, target, market, baseCoin, targetCoin);
    }

    /**
     * 转换列表，仅适合列表中的元素属于同一市场
     *
     * @param sourceList
     * @return
     */
    @Override
    public List<T> toList(List<S> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return super.toList(sourceList);
        }

        MarketView market = marketService.get(resolveMarketId(sourceList.get(0)));
        if (market == null) {
            return super.toList(sourceList);
        }

        CoinView baseCoin = coinService.get(market.getBaseId());
        CoinView targetCoin = coinService.get(market.getTargetId());

        List<T> targetList = new ArrayList<>(sourceList.size());
        sourceList.forEach(source -> {
            T target = createEmptyTarget(source);
            toPojo(source, target, market, baseCoin, targetCoin);
            targetList.add(target);
        });

        return targetList;
    }

    public void toPojo(S source, T target, MarketView market, CoinView baseCoin, CoinView targetCoin) {
        if (source == null || target == null) {
            return;
        }

        BeanUtils.copyProperties(source, target, ignoreFieldsWhileCreating(target));
        convertSpecials(source, target, market, baseCoin, targetCoin);
    }

    /**
     * 获得市场ID
     *
     * @param source
     * @return
     */
    protected abstract String resolveMarketId(S source);

    /**
     * 转换币种相关的数值及其他不能直接拷贝的属性
     *
     * @param source
     * @param target
     * @param market
     * @param baseCoin
     * @param targetCoin
     */
    protected abstract void convertSpecials(S source, T target, MarketView market, CoinView baseCoin,
                                            CoinView targetCoin);

}
