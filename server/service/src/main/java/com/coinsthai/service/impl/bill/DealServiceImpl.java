package com.coinsthai.service.impl.bill;

import com.coinsthai.cache.BillOfUserCache;
import com.coinsthai.converter.SimpleConverter;
import com.coinsthai.model.Bill;
import com.coinsthai.model.Deal;
import com.coinsthai.model.Market;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.pojo.intenum.WalletLogType;
import com.coinsthai.repository.DealRepository;
import com.coinsthai.service.*;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BalanceUpdateRequest;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.bill.DealParameter;
import com.coinsthai.vo.bill.DealSimpleParameter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

/**
 * @author
 */
@Service
public class DealServiceImpl implements DealService {

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private BillService billService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private TrendService trendService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private SimpleConverter<Wallet, Wallet> walletCopier;

    @Autowired
    private BillOfUserCache billOfUserCache;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public Deal create4Sell(final Bill sell, final Wallet sellerTargetWallet, final Wallet sellerBaseWallet,
                            final Bill buy,
                            final Market market) {
        long volume = NumberUtils.min(sell.getRemainVolume(), buy.getRemainVolume());
        long price = buy.getPrice();    //卖单触发，成交价为买单的

        // 创建交易单
        Deal deal = new Deal();
        deal.setType(BillType.SELL);
        deal.setMarket(market);
        deal.setSell(sell);
        deal.setBuy(buy);
        deal.setVolume(volume);
        deal.setPrice(price);
        dealRepository.save(deal);

        // 更新委托单与相关钱包
        Wallet buyerTargetWallet = walletService.getByUserAndCoin(buy.getUser().getId(), market.getTarget().getId());
        Wallet buyerBaseWallet = walletService.getByUserAndCoin(buy.getUser().getId(), market.getBase().getId());
        updateBillsAndWallets(buy,
                              buyerTargetWallet,
                              buyerBaseWallet,
                              sell,
                              sellerTargetWallet,
                              sellerBaseWallet,
                              deal);

        // 更新市场动态和K线
        trendService.updateKlines(market.getId(), deal);
        trendService.updateTrend(market.getId(), deal);

        // 删除买单用户的委托缓存
        billOfUserCache.delete(buy.getUser().getId(), market.getId());

        return deal;
    }

    @Override
    public Deal create4Buy(final Bill buy, final Wallet buyerTargetWallet, final Wallet buyerBaseWallet,
                           final Bill sell, final Market market) {
        long volume = NumberUtils.min(sell.getRemainVolume(), buy.getRemainVolume());
        long price = sell.getPrice();   //买单触发，成交价为卖单的

        // 创建交易单
        Deal deal = new Deal();
        deal.setType(BillType.BUY);
        deal.setMarket(market);
        deal.setSell(sell);
        deal.setBuy(buy);
        deal.setVolume(volume);
        deal.setPrice(price);
        dealRepository.save(deal);

        // 更新委托单与相关钱包
        Wallet sellerTargetWallet = walletService.getByUserAndCoin(sell.getUser().getId(), market.getTarget().getId());
        Wallet sellerBaseWallet = walletService.getByUserAndCoin(sell.getUser().getId(), market.getBase().getId());
        updateBillsAndWallets(buy,
                              buyerTargetWallet,
                              buyerBaseWallet,
                              sell,
                              sellerTargetWallet,
                              sellerBaseWallet,
                              deal);

        // 更新市场动态和K线
        trendService.updateKlines(market.getId(), deal);
        trendService.updateTrend(market.getId(), deal);

        // 删除卖单用户的委托缓存
        billOfUserCache.delete(sell.getUser().getId(), market.getId());
        return deal;
    }

    @Override
    public Page<Deal> pageSimple(DealSimpleParameter parameter) {
        if (parameter.getEndDate() != null) {
            // TODO 处理最后时间点
        }

        return dealRepository.findSimpleByPage(parameter);
    }

    @Override
    public Page<Deal> page(DealParameter parameter) {
        if (parameter.getEndDate() != null) {
            // TODO 处理最后时间点
        }

        return dealRepository.findByPage(parameter);
    }

    private void updateBillsAndWallets(final Bill buy, final Wallet buyerTargetWallet, final Wallet buyerBaseWallet,
                                       final Bill sell, final Wallet sellerTargetWallet, final Wallet sellerBaseWallet,
                                       final Deal deal) {
        CoinView targetCoin = coinService.get(buyerTargetWallet.getCoin().getId());
        CoinView baseCoin = coinService.get(buyerBaseWallet.getCoin().getId());
        long price = deal.getPrice();
        long volume = deal.getVolume();
        long total = CoinNumberUtils.computeTotalPrice(price, volume, targetCoin.getUnit());    //总价，亦即市场基准币种数

        // 计算交易手续费 TODO 判断是否使用平台币扣
        long sellBrokerage = computeBrokerage(total, sell.getBrokerageRate(), baseCoin);
        long buyBrokerage = computeBrokerage(volume, buy.getBrokerageRate(), targetCoin);
        deal.setSellBrokerage(sellBrokerage);
        deal.setBuyBrokerage(buyBrokerage);
        dealRepository.save(deal);

        // 更新卖单
        sell.setDealValue(sell.getDealValue() + total);
        sell.setBrokerage(sell.getBrokerage() + sellBrokerage);
        sell.setRemainVolume(sell.getRemainVolume() - volume);
        if (sell.getRemainVolume() == 0l) {
            sell.setStatus(BillStatus.FINISHED);
            // 计算平均价格
            if (sell.getVolume() == volume) {
                sell.setAveragePrice(price);
            }
            else {
                sell.setAveragePrice(BillUtils.computeAveragePrice(sell, dealRepository));
            }
        }
        billService.update(sell);

        // 更新买单
        buy.setDealValue(buy.getDealValue() + total);
        buy.setBrokerage(buy.getBrokerage() + buyBrokerage);
        buy.setRemainVolume(buy.getRemainVolume() - volume);
        if (buy.getRemainVolume() == 0) {
            buy.setStatus(BillStatus.FINISHED);
            if (buy.getVolume() == volume) {
                buy.setAveragePrice(price);
            }
            else {
                // 计算平均价格
                buy.setAveragePrice(BillUtils.computeAveragePrice(buy, dealRepository));
            }
        }
        billService.update(buy);

        // 更新卖家的市场目标币种余额
        BalanceUpdateRequest stur = createBalanceRequest(sellerTargetWallet, WalletLogType.SELL, volume, deal);
        Wallet updatedSellerTargetWallet = walletService.updateBalanceForDealOut(stur);
        walletCopier.toPojo(updatedSellerTargetWallet, sellerTargetWallet); //将钱包更新

        // 更新卖家的市场基准货币余额
        BalanceUpdateRequest sbur = createBalanceRequest(sellerBaseWallet, WalletLogType.SELL, total, deal);
        Wallet updatedSellerBaseWallet = walletService.updateBalanceForDealIn(sbur, sellBrokerage);
        walletCopier.toPojo(updatedSellerBaseWallet, sellerBaseWallet);

        // 更新买家的市场目标币种余额
        BalanceUpdateRequest btur = createBalanceRequest(buyerTargetWallet, WalletLogType.BUY, volume, deal);
        Wallet updatedBuyerTargetWallet = walletService.updateBalanceForDealIn(btur, buyBrokerage);
        walletCopier.toPojo(updatedBuyerTargetWallet, buyerTargetWallet);

        // 更新买家的市场基准币种余额
        BalanceUpdateRequest bbur = createBalanceRequest(buyerBaseWallet, WalletLogType.BUY, total, deal);
        Wallet updatedBuyerBaseWallet = walletService.updateBalanceForDealOut(bbur);
        walletCopier.toPojo(updatedBuyerBaseWallet, buyerBaseWallet);

        // 解冻买家未使用的冻结额
        if (buy.getStatus() == BillStatus.FINISHED) {
            updatedBuyerBaseWallet = walletService.unfreezeBalance(buyerBaseWallet, buy);
            walletCopier.toPojo(updatedBuyerBaseWallet, buyerBaseWallet);
        }
    }

    // 计算手续费
    private long computeBrokerage(long totalVolume, BigDecimal rate, CoinView coin) {
        if (rate.doubleValue() == 0d) {
            return 0l;
        }

        BigDecimal result = rate.multiply(new BigDecimal(totalVolume));
        if (result.doubleValue() < (double) coin.getMinDealBrokerage()) {
            return coin.getMinDealBrokerage();
        }

        return Math.round(result.doubleValue());
    }

    private BalanceUpdateRequest createBalanceRequest(Wallet wallet, WalletLogType type, long volume, Deal deal) {
        BalanceUpdateRequest request = new BalanceUpdateRequest();
        request.setId(wallet.getId());
        request.setType(type);
        request.setVolume(volume);
        request.setBizId(deal.getId());
        return request;
    }

}
