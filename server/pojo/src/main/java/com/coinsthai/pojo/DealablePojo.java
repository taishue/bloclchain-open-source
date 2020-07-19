package com.coinsthai.pojo;

import com.coinsthai.pojo.common.BasePojo;

/**
 * @author 
 */
public class DealablePojo extends BasePojo {

    private long price;

    private long volume;

    public DealablePojo() {
    }

    public DealablePojo(long price, long volume) {
        this.price = price;
        this.volume = volume;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
