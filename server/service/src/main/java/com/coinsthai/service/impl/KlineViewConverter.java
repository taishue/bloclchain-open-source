package com.coinsthai.service.impl;

import com.coinsthai.converter.BeanCopyPojoConverter;
import com.coinsthai.model.Kline;
import com.coinsthai.vo.KlineView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class KlineViewConverter extends BeanCopyPojoConverter<KlineView, Kline> {

    @Override
    protected void afterBeanCopy(Kline source, KlineView target) {
        super.afterBeanCopy(source, target);
        target.setMarketId(source.getMarket().getId());
    }
}
