package com.coinsthai.pojo.parametric;

import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;

/**
 * @author
 */
public interface BillParametric extends DateRangeParametric {

    BillType getType();

    BillStatus getStatus();

    Boolean getFinished();

    String getUserId();

    String getMarketId();
}
