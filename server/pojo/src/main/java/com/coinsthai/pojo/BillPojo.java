package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;
import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;

import java.math.BigDecimal;

/**
 * @author
 */
public class BillPojo extends ModifiedAtPojo {

    private long volume;

    private long remainVolume;

    private long price;

    private long averagePrice;

    private long dealValue;

    private BigDecimal brokerageRate;

    private long brokerage;

    private BillType type;

    private BillStatus status;

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getRemainVolume() {
        return remainVolume;
    }

    public void setRemainVolume(long remainVolume) {
        this.remainVolume = remainVolume;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(long averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getBrokerageRate() {
        return brokerageRate;
    }

    public void setBrokerageRate(BigDecimal brokerageRate) {
        this.brokerageRate = brokerageRate;
    }

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public long getDealValue() {
        return dealValue;
    }

    public void setDealValue(long dealValue) {
        this.dealValue = dealValue;
    }

    public long getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(long brokerage) {
        this.brokerage = brokerage;
    }
}
