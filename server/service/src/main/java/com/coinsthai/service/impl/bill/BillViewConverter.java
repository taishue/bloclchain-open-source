package com.coinsthai.service.impl.bill;

import com.coinsthai.converter.BeanCopyPojoConverter;
import com.coinsthai.model.Bill;
import com.coinsthai.service.MarketService;
import com.coinsthai.vo.bill.BillView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BillViewConverter extends BeanCopyPojoConverter<BillView, Bill> {

    @Autowired
    private MarketService marketService;

    @Override
    protected void afterBeanCopy(Bill source, BillView target) {
        super.afterBeanCopy(source, target);
        target.setUserId(source.getUser().getId());
        target.setMarketId(source.getMarket().getId());
        target.setMarketName(marketService.get(target.getMarketId()).getName());

    }
}
