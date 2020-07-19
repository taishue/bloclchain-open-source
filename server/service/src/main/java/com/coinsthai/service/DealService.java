package com.coinsthai.service;

import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.model.Market;
import com.coinsthai.model.Wallet;
import com.coinsthai.vo.bill.DealParameter;
import com.coinsthai.vo.bill.DealSimpleParameter;
import org.springframework.data.domain.Page;

/**
 * @author
 */
public interface DealService {

    /**
     * 创建由卖单触发的交易
     * <p>
     * 卖单、钱包等状态会更新
     *
     * @param sell               卖单
     * @param sellerTargetWallet 卖家的市场目标币种钱包
     * @param sellerBaseWallet   卖家的市场基准币种钱包
     * @param buy                买单
     * @param market             市场
     * @return
     */
    Deal create4Sell(final Bill sell, final Wallet sellerTargetWallet, final Wallet sellerBaseWallet, final Bill buy,
                     final Market market);

    /**
     * 创建由买单触发的交易
     * <p>
     * 卖单、钱包等状态会更新
     *
     * @param buy               买单
     * @param buyerTargetWallet 买家的市场目标币种钱包
     * @param buyerBaseWallet   买家的市场基准币种钱包
     * @param sell              卖单
     * @param market            市场
     * @return
     */
    Deal create4Buy(final Bill buy, final Wallet buyerTargetWallet, final Wallet buyerBaseWallet, final Bill sell,
                    final Market market);

    /**
     * 仅查询Deal单表
     *
     * @param parameter
     * @return
     */
    Page<Deal> pageSimple(DealSimpleParameter parameter);

    /**
     * 关联Bill的查询
     *
     * @param parameter
     * @return
     */
    Page<Deal> page(DealParameter parameter);
}
