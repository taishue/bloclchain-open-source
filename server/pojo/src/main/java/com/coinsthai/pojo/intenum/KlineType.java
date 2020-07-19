package com.coinsthai.pojo.intenum;

/**
 * @author 
 */
public enum KlineType implements IntEnum {

    ONE_MINUTE(1),      //1分钟
    FIVE_MINUTES(5),    //5分钟
    ONE_QUARTER(15),    //15分钟
    HALF_HOUR(30),      //半小时
    ONE_HOUR(60),       //1小时
    ONE_DAY(1440);      //1天

    private int number;

    // 毫秒数
    private long millis;

    KlineType(int number) {
        this.number = number;
        this.millis = number * 60 * 1000;
    }

    @Override
    public int getNumber() {
        return number;
    }

    public long getMillis() {
        return millis;
    }
}
