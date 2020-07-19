package com.coinsthai.pojo.intenum;

/**
 * @author
 */
public enum WithdrawStatus implements IntEnum {

    PENDING(0),     //等待处理
    REVOKED(1),     //已撤销
    PROCESSING(2),  //处理中
    DECLINED(3),    //已拒绝
    APPROVED(4),    //已通过
    FINISHED(5);    //已完成

    private int number;

    WithdrawStatus(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
