package com.coinsthai.pojo.intenum;

/**
 * @author
 */
public enum BillStatus implements IntEnum {

    //RECORDED(-1),   //已记录，不参与交易 TODO
    PENDING(0),     //挂单中（含部分成交）
    REVOKED_PART(1),//部分成交且已撤销
    FINISHED(2),    //已全部成交
    REVOKED_ALL(3); //未成交过，已撤销

    private int number;

    BillStatus(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
