package com.coinsthai.btc.impl;

import com.coinsthai.bch.address.AddressConverter;
import com.coinsthai.bitcoin.BitcoinWalletCreator;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.blockchain.CoinType;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BtcWalletCreator extends BitcoinWalletCreator {

    @Override
    public CoinType getCoinType() {
        return CoinType.BTC;
    }

    @Override
    protected void afterWalletCreated(Wallet wallet) {
        String address = wallet.getAddress();
        if (address.startsWith(AddressConverter.PREFIX)) {
            //将bitcoin cash address转成btc address
            wallet.setAddress(AddressConverter.toLegacyAddress(address));
        }
        walletService.create(wallet);
    }
}
