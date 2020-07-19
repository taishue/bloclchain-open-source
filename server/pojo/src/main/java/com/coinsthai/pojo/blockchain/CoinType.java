package com.coinsthai.pojo.blockchain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author YeYifeng
 */
public enum CoinType {
    EQT,

    USDT,
    BTC,
    BCH,
    ETH,
    ETC,
    XRP,
    LTC,
    XLM,

    VEN(ETH),
    OMG(ETH),
    ZIL(ETH),
    ZRX(ETH),
    REP(ETH),
    IVP(ETH),

    DCR,;

    private CoinType tokenOn;

    private CoinType() {
    }

    private CoinType(CoinType tokenOn) {
        this.tokenOn = tokenOn;
    }

    public CoinType getTokenOn() {
        return tokenOn;
    }

    /**
     * 获得代币列表
     *
     * @return
     */
    public List<CoinType> getTokens() {
        List<CoinType> tokens = new ArrayList<>();
        for (CoinType type : values()) {
            if (this.equals(type.getTokenOn())) {
                tokens.add(type);
            }
        }
        return tokens;
    }
}
