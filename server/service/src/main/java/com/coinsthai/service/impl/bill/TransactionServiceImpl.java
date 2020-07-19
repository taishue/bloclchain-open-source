package com.coinsthai.service.impl.bill;

import com.coinsthai.cache.BillHighestCache;
import com.coinsthai.cache.BillLowestCache;
import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.model.Market;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.repository.BillRepository;
import com.coinsthai.service.BillService;
import com.coinsthai.service.DealService;
import com.coinsthai.service.TransactionService;
import com.coinsthai.service.WalletService;
import com.coinsthai.vo.bill.BillCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private BillService billService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private DealService dealService;

    @Autowired
    private BillLowestCache billLowestCache;

    @Autowired
    private BillHighestCache billHighestCache;

    @Override
    public Bill createBill(BillCreateRequest request) {
        Bill bill = billService.create(request);

        Market market = bill.getMarket();
        Wallet targetWallet = walletService.getByUserAndCoin(bill.getUser().getId(), market.getTarget().getId());
        Wallet baseTarget = walletService.getByUserAndCoin(bill.getUser().getId(), market.getBase().getId());

        boolean txFinished = false;
        do {
            if (bill.getType() == BillType.SELL) {
                txFinished = transaction4Sell(bill, targetWallet, baseTarget, market);
            }
            else {
                txFinished = transaction4Buy(bill, targetWallet, baseTarget, market);
            }
        } while (!txFinished);

        // 增加在挂单缓存中
        if (bill.getType() == BillType.SELL) {
            billLowestCache.add(bill);
        }
        else {
            billHighestCache.add(bill);
        }

        return bill;
    }

    /**
     * 创建由卖单触发的交易
     *
     * @return 如果处理结束即返回true，否则false
     */
    private boolean transaction4Sell(Bill sell, Wallet sellerTargetWallet, Wallet sellerBaseWallet, Market market) {
        // 获得前20条符合条件的记录
        List<Bill> buys = billRepository.findMatchBuys(sell.getMarket().getId(), sell.getPrice());
        if (buys.size() == 0) {
            if (sell.getPrice() == 0) {
                //市价卖单，在没有匹配的买单后，要设置限价
                billService.updatePrice4Sell(sell);
            }
            return true;
        }

        for (Bill buy : buys) {
            try {
                Deal deal = dealService.create4Sell(sell, sellerTargetWallet, sellerBaseWallet, buy, market);
                billHighestCache.remove(deal);  //更新最高买单缓存
            } catch (Exception e) {
                LOGGER.error("Failed to create deal.", e);
                continue;
            }

            if (sell.getRemainVolume() == 0l || sell.getStatus() != BillStatus.PENDING) {
                return true;
            }
        }

        // 从返回记录数是否小于每页数，可减少一次查询
        if (buys.size() < BillRepository.BILL_COUNT_TO_DEAL) {
            return true;
        }

        return false;
    }

    /**
     * 创建由买单触发的交易
     *
     * @return 如果处理结束即返回true，否则false
     */
    private boolean transaction4Buy(Bill buy, Wallet buyerTargetWallet, Wallet buyerBaseWallet, Market market) {
        // 获得前20条符合条件的记录
        List<Bill> sells = billRepository.findMatchSells(buy.getMarket().getId(), buy.getPrice());
        if (sells.size() == 0) {
            if (buy.getPrice() == 0) {
                //市价买单，在没有匹配的卖单后，要设置限价
                billService.updatePrice4Buy(buy, buyerBaseWallet);
            }
            return true;
        }

        for (Bill sell : sells) {
            try {
                Deal deal = dealService.create4Buy(buy, buyerTargetWallet, buyerBaseWallet, sell, market);
                billLowestCache.remove(deal);   //更新最低卖单缓存
            } catch (Exception e) {
                LOGGER.error("Failed to create deal.", e);
                continue;
            }

            if (buy.getRemainVolume() == 0l || buy.getStatus() != BillStatus.PENDING) {
                return true;
            }
        }

        // 从返回记录数是否小于每页数，可减少一次查询
        if (sells.size() < BillRepository.BILL_COUNT_TO_DEAL) {
            return true;
        }

        return false;
    }

}
