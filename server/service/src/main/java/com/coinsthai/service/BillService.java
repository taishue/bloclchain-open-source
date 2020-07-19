package com.coinsthai.service;

import com.coinsthai.model.Bill;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.vo.bill.BillCreateRequest;
import com.coinsthai.vo.bill.BillParameter;
import com.coinsthai.vo.bill.BillSimpleView;
import com.coinsthai.vo.bill.BillView;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author 
 */
public interface BillService {

    /**
     * 请求创建委托单
     * 请求将放入消息队列中
     *
     * @param request
     */
    void requestCreate(BillCreateRequest request);

    Bill create(BillCreateRequest request);

    Bill update(Bill bill);

    /**
     * 更新市价卖单的限价
     * 在主动匹配未全部成交的情况下，需要设置限价
     *
     * @param bill
     * @return
     */
    Bill updatePrice4Sell(Bill bill);

    /**
     * 更新市价买单的限价
     * 在主动匹配未全部成交的情况下，需要设置限价，并冻结相关钱包余额
     *
     * @param bill   委托单
     * @param wallet 市场基准货币的钱包
     * @return
     */
    Bill updatePrice4Buy(Bill bill, Wallet wallet);

    Bill revoke(String id);

    Bill revoke(Bill bill);

    Bill get(String id);

    Page<Bill> page(BillParameter parameter);

    /**
     * 获得待处理卖单中前20个最低价格及数量
     *
     * @return
     */
    List<BillSimpleView> listLowestPendingSells(String marketId);

    /**
     * 获得待处理买单中前20个最高价格及数量
     *
     * @return
     */
    List<BillSimpleView> listHighestPendingBuys(String marketId);

    /**
     * 获得用户最新的委托单
     *
     * @param userId
     * @param marketId
     * @param finished
     * @return
     */
    List<BillView> listLatestOfUser(String userId, String marketId, boolean finished);
}
