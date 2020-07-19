package com.coinsthai.ltc.impl;

import com.coinsthai.bitcoin.BitcoinRpcConfigProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class LtcConfig implements BitcoinRpcConfigProvider {

    @Value("${app.ltc.wallet.path}")
    private String walletPath;

    @Value("${app.ltc.wallet.password}")
    private String walletPassword;

    @Value("${app.ltc.wallet.random.salt}")
    private String randomSalt;  //TODO random salt是否应该每次随机生成

    @Value("${app.ltc.rpc.url}")
    private String rpcUrl;

    @Value("${app.ltc.rpc.query.limit}")
    private int queryLimit;

    @Value(("${app.ltc.transaction.confirmations}"))
    private int minConfirmations;

    @Value("${app.ltc.coin.unit}")
    private int coinUnit;


    public String getWalletPath() {
        return walletPath;
    }

    public String getWalletPassword() {
        return walletPassword;
    }

    public String getRandomSalt() {
        return randomSalt;
    }

    @Override
    public String getRpcUrl() {
        return rpcUrl;
    }

    @Override
    public int getQueryLimit() {
        return queryLimit;
    }

    @Override
    public int getMinConfirmations() {
        return minConfirmations;
    }

    @Override
    public int getCoinUnit() {
        return coinUnit;
    }
}
