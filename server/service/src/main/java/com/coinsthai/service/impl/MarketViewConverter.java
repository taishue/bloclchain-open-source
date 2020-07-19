package com.coinsthai.service.impl;

import com.coinsthai.converter.BeanCopyPojoConverter;
import com.coinsthai.model.Market;
import com.coinsthai.service.CoinService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class MarketViewConverter extends BeanCopyPojoConverter<MarketView, Market> {

    @Autowired
    private CoinService coinService;

    @Override
    protected void afterBeanCopy(Market source, MarketView target) {
        target.setTargetId(source.getTarget().getId());
        CoinView t = coinService.get(source.getTarget().getId());
        if (t != null) {
            target.setTargetName(t.getName());
        }

        target.setBaseId(source.getBase().getId());
        CoinView b = coinService.get(source.getBase().getId());
        if (b != null) {
            target.setBaseName(b.getName());
        }
    }
}
