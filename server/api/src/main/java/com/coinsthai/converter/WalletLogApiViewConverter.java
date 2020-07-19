package com.coinsthai.converter;

import com.coinsthai.model.WalletLog;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.WalletLogApiView;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class WalletLogApiViewConverter extends CoinableConverter<WalletLogApiView, WalletLog> {

    @Override
    protected String resolveCoinId(WalletLog source) {
        return source.getWallet().getCoin().getId();
    }

    @Override
    protected void convertSpecials(WalletLog source, WalletLogApiView target, CoinView coin) {
        target.setVolume(CoinNumberUtils.formatDoubleVolume(source.getVolume(), coin.getUnit()));
        target.setAvailableBalance(CoinNumberUtils.formatDoubleVolume(source.getAvailableBalance(), coin.getUnit()));
        target.setFrozenBalance(CoinNumberUtils.formatDoubleVolume(source.getFrozenBalance(), coin.getUnit()));
        target.setPendingBalance(CoinNumberUtils.formatDoubleVolume(source.getPendingBalance(), coin.getUnit()));

        target.setWalletId(source.getWallet().getId());
        target.setCoinId(coin.getId());
        target.setCoinName(coin.getName());
    }
}
