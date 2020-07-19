package com.coinsthai.service.impl.bill;

import com.coinsthai.converter.BeanCopyPojoConverter;
import com.coinsthai.model.Bill;
import com.coinsthai.vo.bill.BillSimpleView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BillSimpleViewConverter extends BeanCopyPojoConverter<BillSimpleView, Bill> {

    @Override
    protected void afterBeanCopy(Bill source, BillSimpleView target) {
        super.afterBeanCopy(source, target);
        target.setVolume(source.getRemainVolume());
    }
}
