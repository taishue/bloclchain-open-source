package com.coinsthai.pojo.parametric;

import com.coinsthai.pojo.intenum.BillType;

/**
 * @author
 */
public interface DealSimpleParametric extends DateRangeParametric {

    BillType getType();

    String getMarketId();

}
