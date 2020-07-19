package com.coinsthai.pojo.intenum;

/**
 * @author
 */
public enum BillType implements IntEnum {

    SELL(0),    //卖单
    BUY(1);     //买单

    private int number;

    BillType(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
