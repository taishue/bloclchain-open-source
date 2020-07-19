package com.coinsthai.service.impl.bill;

import com.coinsthai.cache.*;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Bill;
import com.coinsthai.model.Market;
import com.coinsthai.model.User;
import com.coinsthai.model.Wallet;
import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.edm.event.BillCreationRequestEvent;
import com.coinsthai.pojo.common.Constant;
import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.pojo.intenum.WalletLogType;
import com.coinsthai.repository.BillRepository;
import com.coinsthai.repository.DealRepository;
import com.coinsthai.repository.MarketRepository;
import com.coinsthai.service.*;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BalanceUpdateRequest;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketTrendView;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.bill.BillCreateRequest;
import com.coinsthai.vo.bill.BillParameter;
import com.coinsthai.vo.bill.BillSimpleView;
import com.coinsthai.vo.bill.BillView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author
 */
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillViewConverter billViewConverter;

    @Autowired
    private BillSimpleViewConverter billSimpleViewConverter;

    @Autowired
    private MarketService marketService;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private MarketTrendCache marketTrendCache;

    @Autowired
    private MarketTrendExternalCache marketTrendExternalCache;

    @Autowired
    private BillOfUserCache billOfUserCache;

    @Autowired
    private BillLowestCache billLowestCache;

    @Autowired
    private BillHighestCache billHighestCache;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ServiceChargeService serviceChargeService;

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private AuditLogger auditLogger;


    @Override
    public void requestCreate(BillCreateRequest request) {
        MarketView market = marketService.get(request.getMarketId());
        CoinView coin = coinService.get(market.getTargetId());
        if (coin.getMinDeal() > request.getVolume()) {
            BizException ex = new BizException(BizErrorCode.BILL_VOLUME_TOO_SMALL,
                                               "volume,minDeal",
                                               request.getVolume(),
                                               coin.getMinDeal());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }

        User user = userService.get(request.getUserId());
        if (!user.isRobot() && !user.isIdVerify()) {
            BizException ex = new BizException(BizErrorCode.BILL_USER_UNVERIFIED,
                                               "userId",
                                               request.getUserId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }

        eventPublisher.publish(new BillCreationRequestEvent(request));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    public Bill create(BillCreateRequest request) {
        checkBill(request);

        MarketView marketView = marketService.get(request.getMarketId());
        //FIXME active暂时放在controller层判断，因为要放开机器人
//        if (marketView == null || !marketView.isActive()) {
//            BizException ex = new BizException(BizErrorCode.BILL_MARKET_CLOSE);
//            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
//        }

        User user = getAndCheckUser(request.getUserId());
        Bill bill = null;
        if (request.getType() == BillType.SELL) {
            bill = createSell(request, user, marketView);
        }
        else {
            bill = createBuy(request, user, marketView);
        }
        auditLogger.success(AuditAction.CREATED, AuditEntity.BILL, bill.getId());

        // 清除用户最新委托单缓存
        billOfUserCache.delete(bill.getUser().getId(), bill.getMarket().getId(), false);

        return bill;
    }

    @Transactional
    @Override
    public Bill update(Bill bill) {
        billRepository.save(bill);
        auditLogger.success(AuditAction.UPDATED, AuditEntity.BILL, bill.getId());

        // 清除用户最新委托单缓存
        billOfUserCache.delete(bill.getUser().getId(), bill.getMarket().getId());

        return bill;
    }

    @Transactional
    @Override
    public Bill updatePrice4Sell(Bill bill) {
        //  获得当前市场最新成交价，并更新限价
        bill.setPrice(getLatestPrice(bill.getMarket().getId()));

        // 市价卖单创建时已冻结钱包余额
        return update(bill);
    }

    @Transactional
    @Override
    public Bill updatePrice4Buy(Bill bill, Wallet wallet) {
        //  获得当前市场最新成交价，并更新限价
        long newPrice = getLatestPrice(bill.getMarket().getId());
        if (wallet.getAvailableBalance() < newPrice * bill.getRemainVolume()) {
            // 余额不足，撤单
            return revoke(bill);
        }

        // 更新价格
        bill.setPrice(newPrice);
        bill = update(bill);

        // 根据剩余的数量冻结要支付币种的钱包余额
        freezeBalance(wallet.getId(), newPrice * bill.getVolume(), bill);

        return bill;
    }

    private long getLatestPrice(String marketId) {
        MarketTrendView view = marketTrendCache.get(marketId);
        if (view == null) {
            view = marketTrendExternalCache.get(marketId);
        }

        return view.getLast();
    }

    @Override
    public Bill revoke(String id) {
        Bill bill = get(id);
        if (bill == null) {
            BizException ex = new BizException(BizErrorCode.BILL_NOT_FOUND, "id", id);
            auditLogger.fail("revoke", AuditEntity.BILL, bill.getId(), ex);
        }

        return revoke(bill);
    }

    @Override
    public Bill revoke(Bill bill) {
        if (bill.getStatus() != BillStatus.PENDING) {
            BizException ex = new BizException(BizErrorCode.BILL_STATUS_ILLEGAL,
                                               "id,status",
                                               bill.getId(),
                                               bill.getStatus());
            auditLogger.fail("revoke", AuditEntity.BILL, bill.getId(), ex);
        }

        if (bill.getVolume() == bill.getRemainVolume()) {
            bill.setStatus(BillStatus.REVOKED_ALL);
        }
        else {
            bill.setStatus(BillStatus.REVOKED_PART);
            bill.setAveragePrice(BillUtils.computeAveragePrice(bill, dealRepository));
        }
        bill = billRepository.save(bill);

        // 解冻余额
        walletService.unfreezeBalance(bill);

        auditLogger.success("revoke", AuditEntity.BILL, bill.getId());

        // 清除用户最新委托单缓存
        billOfUserCache.delete(bill.getUser().getId(), bill.getMarket().getId());
        // 更新最低最高委托缓存
        if (bill.getType() == BillType.SELL) {
            billLowestCache.remove(bill);
        }
        else {
            billHighestCache.remove(bill);
        }

        return bill;
    }

    private Bill createSell(BillCreateRequest request, User user, MarketView marketView) {
        Wallet wallet = walletService.getByUserAndCoin(user.getId(), marketView.getTargetId());
        if (wallet == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_NOT_FOUND,
                                               "userId, coinId",
                                               user.getId(),
                                               marketView.getTargetId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }

        if (wallet.getAvailableBalance() < request.getVolume()) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "walletId,balance,volume",
                                               wallet.getId(),
                                               wallet.getAvailableBalance(),
                                               request.getVolume());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }

        Bill bill = createBill(request, user);

        // 冻结要售出币种的余额
        freezeBalance(wallet.getId(), bill.getRemainVolume(), bill);

        return bill;
    }

    private Wallet freezeBalance(String walletId, long volume, Bill bill) {
        BalanceUpdateRequest balanceUpdateRequest = new BalanceUpdateRequest();
        balanceUpdateRequest.setId(walletId);
        balanceUpdateRequest.setVolume(volume);
        balanceUpdateRequest.setType(WalletLogType.FREEZE);
        balanceUpdateRequest.setBizId(bill.getId());
        return walletService.freezeBalance(balanceUpdateRequest);
    }

    private Bill createBuy(BillCreateRequest request, User user, MarketView marketView) {
        Wallet wallet = walletService.getByUserAndCoin(user.getId(), marketView.getBaseId());
        if (wallet == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "walletId",
                                               wallet.getId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }

        if (request.getPrice() == 0l) {
            // 市价，暂不冻结余额
            return createBill(request, user);
        }

        CoinView targetCoin = coinService.get(marketView.getTargetId());
        long total = CoinNumberUtils.computeTotalPrice(request.getPrice(), request.getVolume(), targetCoin.getUnit());
        if (wallet.getAvailableBalance() < total) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "walletId,balance,price,volume",
                                               wallet.getId(),
                                               wallet.getAvailableBalance(),
                                               request.getPrice(),
                                               request.getVolume());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
        Bill bill = createBill(request, user);

        // 冻结要支付币种的余额
        freezeBalance(wallet.getId(), total, bill);

        return bill;
    }

    private Bill createBill(BillCreateRequest request, User user) {
        Bill bill = new Bill();
        BeanUtils.copyProperties(request, bill);
        bill.setRemainVolume(bill.getVolume());
        bill.setStatus(BillStatus.PENDING);
        bill.setUser(user);
        bill.setMarket(marketRepository.findOne(request.getMarketId()));

        // 手续费率在挂单时确定
        bill.setBrokerageRate(computeBrokerageRate(bill.getMarket(), user));

        return billRepository.save(bill);
    }

    // 计算手续费率
    private BigDecimal computeBrokerageRate(Market market, User user) {
        BigDecimal marketDiscount = serviceChargeService.getDiscountByMarket(market);
        BigDecimal userDiscount = serviceChargeService.getDiscountByUser(user);
        BigDecimal rate = serviceChargeService.getBaseBrokerageRate().multiply(marketDiscount).multiply(userDiscount);
        return rate.setScale(Constant.BROKERAGE_RATE_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    private User getAndCheckUser(String userId) {
        User user = userService.get(userId);
        if (user == null) {
            BizException ex = new BizException(BizErrorCode.USER_NOT_FOUND, "id", userId);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
        if (!user.isActive()) {
            BizException ex = new BizException(BizErrorCode.USER_NOT_ACTIVE, "id", userId);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }

        return user;
    }

    private void checkBill(BillCreateRequest request) {
        if (StringUtils.isBlank(request.getUserId())) {
            BizException ex = new BizException(BizErrorCode.BILL_USER_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
        if (StringUtils.isBlank(request.getMarketId())) {
            BizException ex = new BizException(BizErrorCode.BILL_MARKET_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
        if (request.getType() == null) {
            BizException ex = new BizException(BizErrorCode.BILL_TYPE_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
        if (request.getVolume() <= 0l) {
            BizException ex = new BizException(BizErrorCode.BILL_VOLUME_ILLEGAL,
                                               "volume",
                                               request.getVolume());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
        if (request.getPrice() < 0l) {
            BizException ex = new BizException(BizErrorCode.BILL_PRICE_ILLEGAL,
                                               "price",
                                               request.getPrice());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.BILL, "", ex);
        }
    }

    @Override
    public Bill get(String id) {
        return billRepository.findOne(id);
    }

    @Override
    public Page<Bill> page(BillParameter parameter) {
        if (parameter.getEndDate() != null) {
            // TODO 处理最后时间点
        }

        return billRepository.findByPage(parameter);
    }

    @Override
    public List<BillSimpleView> listLowestPendingSells(String marketId) {
        if (billLowestCache.cached(marketId)) {
            return billLowestCache.get(marketId);
        }

        List<Bill> list = billRepository.findLowestPendingSells(marketId);
        Collections.reverse(list); //将卖单价格由高到低排
        List<BillSimpleView> views = billSimpleViewConverter.toList(list);
        billLowestCache.set(marketId, views);

        return views;
    }

    @Override
    public List<BillSimpleView> listHighestPendingBuys(String marketId) {
        if (billHighestCache.cached(marketId)) {
            return billHighestCache.get(marketId);
        }

        List<Bill> list = billRepository.findHighestPendingBuys(marketId);
        List<BillSimpleView> views = billSimpleViewConverter.toList(list);
        billHighestCache.set(marketId, views);

        return views;
    }

    @Override
    public List<BillView> listLatestOfUser(String userId, String marketId, boolean finished) {
        if (StringUtils.isAnyBlank(userId, marketId)) {
            throw new BizException(BizErrorCode.BILL_MARKET_EMPTY);
        }

        if (billOfUserCache.cached(userId, marketId, finished)) {
            return billOfUserCache.get(userId, marketId, finished);
        }

        BillParameter parameter = new BillParameter();
        parameter.setUserId(userId);
        parameter.setMarketId(marketId);
        parameter.setFinished(finished);
        Page<Bill> modelPage = page(parameter);

        List<BillView> views = billViewConverter.toList(modelPage.getContent());
        billOfUserCache.set(userId, marketId, finished, views);

        return views;
    }
}
