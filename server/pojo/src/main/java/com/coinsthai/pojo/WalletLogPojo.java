package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;
import com.coinsthai.pojo.intenum.WalletLogType;

/**
 * @author
 */
public class WalletLogPojo extends ModifiedAtPojo {

    // 小于0为减少
    private long volume;

    private long availableBalance;

    private long frozenBalance;

    private long pendingBalance;

    private WalletLogType type;

    private String bizId;

    private String description;

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

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

    public WalletLogType getType() {
        return type;
    }

    public void setType(WalletLogType type) {
        this.type = type;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
