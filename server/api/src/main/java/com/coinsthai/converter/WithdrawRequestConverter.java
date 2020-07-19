package com.coinsthai.converter;

import com.coinsthai.service.WalletService;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.WithdrawApiRequest;
import com.coinsthai.vo.wallet.WithdrawRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class WithdrawRequestConverter extends CoinableConverter<WithdrawRequest, WithdrawApiRequest> {

    @Autowired
    private WalletService walletService;

    @Override
    protected String resolveCoinId(WithdrawApiRequest source) {
        return walletService.get(source.getWalletId()).getCoin().getId();
    }

    @Override
    protected void convertSpecials(WithdrawApiRequest source, WithdrawRequest target, CoinView coin) {
        target.setVolume(CoinNumberUtils.parseLong(source.getVolume(), coin.getUnit()));
        target.setBrokerage(CoinNumberUtils.parseLong(source.getBrokerage(), coin.getUnit()));

        target.setWalletId(source.getWalletId());
    }
}
