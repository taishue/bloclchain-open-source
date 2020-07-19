package com.coinsthai.pojo;

import com.coinsthai.pojo.common.ModifiedAtPojo;
import com.coinsthai.pojo.intenum.BillType;

/**
 * @author
 */
public class DealPojo extends ModifiedAtPojo {

    private long volume;

    private long price;

    private long sellBrokerage;

    private long buyBrokerage;

    private BillType type;

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getSellBrokerage() {
        return sellBrokerage;
    }

    public void setSellBrokerage(long sellBrokerage) {
        this.sellBrokerage = sellBrokerage;
    }

    public long getBuyBrokerage() {
        return buyBrokerage;
    }

    public void setBuyBrokerage(long buyBrokerage) {
        this.buyBrokerage = buyBrokerage;
    }

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }
}
