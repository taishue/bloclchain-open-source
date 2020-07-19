package com.coinsthai.cache;

import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.vo.bill.BillSimpleView;

import java.util.List;

/**
 * @author
 */
public interface BillLowestCache {

    int MAX_LENGTH = 10;

    List<BillSimpleView> get(String marketId);

    boolean cached(String marketId);

    void set(String marketId, List<BillSimpleView> list);

    /**
     * 指定的委托如果小于等于已有价格，即增加到缓存中
     *
     * @param bill
     */
    void add(Bill bill);

    /**
     * 撤单时，将相应的价格和数量从缓存中移出
     *
     * @param bill
     */
    void remove(Bill bill);

    /**
     * 将交易单中相应的价格和数量从缓存中移出
     *
     * @param deal
     */
    void remove(Deal deal);

}
