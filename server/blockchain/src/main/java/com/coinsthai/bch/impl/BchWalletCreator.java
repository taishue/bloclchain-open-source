package com.coinsthai.bch.impl;

import com.coinsthai.bch.BchService;
import com.coinsthai.bch.address.AddressConverter;
import com.coinsthai.bitcoin.BitcoinWalletCreator;
import com.coinsthai.blockchain.WalletCreator;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.vo.wallet.WalletCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class BchWalletCreator extends BitcoinWalletCreator {

    @Autowired
    private BchService bchService;

    @Override
    protected void afterWalletCreated(Wallet wallet) {
        String bchAddress = AddressConverter.toCashAddress(wallet.getAddress());
        wallet.setAddress(bchAddress);
        walletService.create(wallet);

        // upload address to bch fullnode
        bchService.uploadAddress(wallet.getUser().getId(), bchAddress);
    }

    @Override
    public CoinType getCoinType() {
        return CoinType.BCH;
    }
}
