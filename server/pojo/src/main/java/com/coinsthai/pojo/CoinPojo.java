package com.coinsthai.pojo;

import com.coinsthai.pojo.common.NamedModifiedAtPojo;
import com.coinsthai.pojo.intenum.CoinCategory;

/**
 * @author
 */
public class CoinPojo extends NamedModifiedAtPojo {

    private String fullName;

    private CoinCategory category;

    private String contract;

    private boolean base;

    private int unit;

    private long minDeal;

    private long minDealBrokerage;

    private long minWithdraw;

    private long minNetworkBrokerage;

    private long maxNetworkBrokerage;

    private int priority;

    private boolean active;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public CoinCategory getCategory() {
        return category;
    }

    public void setCategory(CoinCategory category) {
        this.category = category;
    }

    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getMinDeal() {
        return minDeal;
    }

    public void setMinDeal(long minDeal) {
        this.minDeal = minDeal;
    }

    public long getMinDealBrokerage() {
        return minDealBrokerage;
    }

    public void setMinDealBrokerage(long minDealBrokerage) {
        this.minDealBrokerage = minDealBrokerage;
    }

    public long getMinWithdraw() {
        return minWithdraw;
    }

    public void setMinWithdraw(long minWithdraw) {
        this.minWithdraw = minWithdraw;
    }

    public long getMinNetworkBrokerage() {
        return minNetworkBrokerage;
    }

    public void setMinNetworkBrokerage(long minNetworkBrokerage) {
        this.minNetworkBrokerage = minNetworkBrokerage;
    }

    public long getMaxNetworkBrokerage() {
        return maxNetworkBrokerage;
    }

    public void setMaxNetworkBrokerage(long maxNetworkBrokerage) {
        this.maxNetworkBrokerage = maxNetworkBrokerage;
    }
}
