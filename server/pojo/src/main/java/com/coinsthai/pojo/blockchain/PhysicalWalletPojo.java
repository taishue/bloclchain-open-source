package com.coinsthai.pojo.blockchain;

import com.coinsthai.pojo.common.ModifiedAtPojo;

/**
 * @author
 */
public class PhysicalWalletPojo extends ModifiedAtPojo {

    private CoinType coinType;

    private String address;

    private long balance;

    public CoinType getCoinType() {
        return coinType;
    }

    public void setCoinType(CoinType coinType) {
        this.coinType = coinType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
