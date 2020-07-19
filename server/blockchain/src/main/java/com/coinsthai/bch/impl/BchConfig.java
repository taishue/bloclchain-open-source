package com.coinsthai.bch.impl;

import com.coinsthai.bitcoin.BitcoinRpcConfigProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class BchConfig implements BitcoinRpcConfigProvider {

    @Value("${app.bch.rpc.url}")
    private String rpcUrl;

    @Value("${app.bch.rpc.query.limit}")
    private int queryLimit;

    @Value(("${app.bch.transaction.confirmations}"))
    private int minConfirmations;

    @Value("${app.bch.coin.unit}")
    private int coinUnit;

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
