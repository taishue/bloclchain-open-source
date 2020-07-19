package com.coinsthai.pojo.intenum;

/**
 * @author
 */
public enum WalletLogType implements IntEnum {

    SELL(0),       //卖
    RECHARGE(1),   //充值
    PRE_RECHARGE(2),//预冲值

    BUY(3),        //买
    DEAL_BROKERAGE(4),  //交易手续费
    WITHDRAW(5),   //提现
    WITHDRAW_BROKERAGE(6),  //提现手续费

    FREEZE(7),     //冻结
    UNFREEZE(8),   //解冻

    ADJUST(9);     //系统调整

    private int number;

    WalletLogType(int number) {
        this.number = number;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
