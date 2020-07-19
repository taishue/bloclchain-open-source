package com.coinsthai.service.impl;

import com.coinsthai.converter.BeanCopyPojoConverter;
import com.coinsthai.model.Coin;
import com.coinsthai.model.Market;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class MarketConverter extends BeanCopyPojoConverter<Market, MarketView> {

    @Autowired
    private ModelFactory em;

    @Override
    protected void afterBeanCopy(MarketView source, Market target) {
        target.setTarget(em.getReference(Coin.class, source.getTargetId()));
        target.setBase(em.getReference(Coin.class, source.getBaseId()));
    }
}
