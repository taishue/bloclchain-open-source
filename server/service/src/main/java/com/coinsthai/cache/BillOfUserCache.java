package com.coinsthai.cache;

import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.vo.bill.BillView;

import java.util.List;

/**
 * @author
 */
public interface BillOfUserCache {

    void set(String userId, String marketId, boolean finished, List<BillView> bills);

    void delete(String userId, String marketId, boolean finished);

    void delete(String userId, String marketId);

    List<BillView> get(String userId, String marketId, boolean finished);

    boolean cached(String userId, String marketId, boolean finished);

}
