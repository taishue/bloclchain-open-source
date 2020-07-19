package com.coinsthai.pojo.parametric;

import com.coinsthai.pojo.intenum.WalletLogType;

/**
 * @author 
 */
public interface WalletLogParametric extends DateRangeParametric {

    String getUserId();

    String getWalletId();

    WalletLogType getType();
}
