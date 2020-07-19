package com.coinsthai.pojo.intenum;

import com.coinsthai.pojo.intenum.IntEnum;

/**
 * @author
 */
public enum CoinCategory implements IntEnum {

    DIGITAL(0),     //数字币
    LEGAL(1);       //法币

    private int number;

    CoinCategory(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
