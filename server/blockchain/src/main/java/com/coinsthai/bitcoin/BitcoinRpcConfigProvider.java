package com.coinsthai.bitcoin;

/**
 * @author
 */
public interface BitcoinRpcConfigProvider {

    String getRpcUrl();

    int getQueryLimit();

    int getMinConfirmations();

    int getCoinUnit();
}
