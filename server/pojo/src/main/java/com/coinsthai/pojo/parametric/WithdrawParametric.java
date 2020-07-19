package com.coinsthai.pojo.parametric;

import com.coinsthai.pojo.intenum.WithdrawStatus;

/**
 * @author
 */
public interface WithdrawParametric extends DateRangeParametric {

    WithdrawStatus getStatus();

    String getUserId();

    String getCoinId();
}
