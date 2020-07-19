package com.coinsthai.converter;

import com.coinsthai.model.Withdraw;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.WithdrawApiView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class WithdrawApiViewConverter extends CoinableConverter<WithdrawApiView, Withdraw> {

    @Override
    protected String resolveCoinId(Withdraw source) {
        return source.getCoin().getId();
    }

    @Override
    protected void convertSpecials(Withdraw source, WithdrawApiView target, CoinView coin) {
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getVolume(), coin.getUnit()));
        target.setBrokerage(CoinNumberUtils.formatDoubleVolume(source.getBrokerage(), coin.getUnit()));

        target.setUserId(source.getUser().getId());
        target.setWalletId(source.getWallet().getId());
        target.setCoinId(coin.getId());
        target.setCoinName(coin.getName());
    }
}
