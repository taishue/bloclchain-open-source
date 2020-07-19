package com.coinsthai.converter;

import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinApiView;
import com.coinsthai.vo.CoinView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class CoinApiViewConverter extends BeanCopyPojoConverter<CoinApiView, CoinView> {

    @Override
    protected void afterBeanCopy(CoinView source, CoinApiView target) {
        super.afterBeanCopy(source, target);

        int unit = source.getUnit();
        target.setMinDeal(CoinNumberUtils.formatDoubleVolume(source.getMinDeal(), unit));
        target.setMinDealBrokerage(CoinNumberUtils.formatDoubleVolume(source.getMinDealBrokerage(), unit));
        target.setMinWithdraw(CoinNumberUtils.formatDoubleVolume(source.getMinWithdraw(), unit));
        target.setMinNetworkBrokerage(CoinNumberUtils.formatDoubleVolume(source.getMinNetworkBrokerage(), unit));
        target.setMaxNetworkBrokerage(CoinNumberUtils.formatDoubleVolume(source.getMaxNetworkBrokerage(), unit));
    }
}
