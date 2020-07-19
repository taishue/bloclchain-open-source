package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;
import com.coinsthai.pojo.intenum.WithdrawStatus;

/**
 * @author
 */
public class WithdrawPojo extends ModifiedAtPojo {

    private String address;

    private long volume;

    private long brokerage;

    private String txid;

    private WithdrawStatus status;

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(long brokerage) {
        this.brokerage = brokerage;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }
}
