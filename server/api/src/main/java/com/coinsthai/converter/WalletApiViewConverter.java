package com.coinsthai.converter;

import com.coinsthai.model.Wallet;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.WalletApiView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class WalletApiViewConverter extends CoinableConverter<WalletApiView, Wallet> {

    @Override
    protected String resolveCoinId(Wallet source) {
        return source.getCoin().getId();
    }

    @Override
    protected void convertSpecials(Wallet source, WalletApiView target, CoinView coin) {
        target.setAvailableBalance(CoinNumberUtils.formatDoubleVolume(source.getAvailableBalance(), coin.getUnit()));
        target.setFrozenBalance(CoinNumberUtils.formatDoubleVolume(source.getFrozenBalance(), coin.getUnit()));
        target.setPendingBalance(CoinNumberUtils.formatDoubleVolume(source.getPendingBalance(), coin.getUnit()));

        target.setUserId(source.getUser().getId());
        target.setCoinId(coin.getId());
        target.setCoinName(coin.getName());
        target.setCoinCategory(coin.getCategory());
    }
}
