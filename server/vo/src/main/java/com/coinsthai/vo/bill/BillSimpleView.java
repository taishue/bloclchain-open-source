package com.coinsthai.vo.bill;

import com.coinsthai.pojo.common.StringIdentifier;
import com.coinsthai.pojo.intenum.BillType;

/**
 * @author
 */
public class BillSimpleView extends StringIdentifier {

    private long volume;

    private long price;

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

    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }
}
