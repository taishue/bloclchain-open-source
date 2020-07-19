package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;

/**
 * @author
 */
public class WalletPojo extends ModifiedAtPojo {

    private long availableBalance;

    private long frozenBalance;

    private long pendingBalance;

    private String address;

    public long getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(long availableBalance) {
        this.availableBalance = availableBalance;
    }

    public long getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(long frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    public long getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(long pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
