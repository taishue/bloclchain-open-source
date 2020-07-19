package com.coinsthai.service.impl.wallet;

import com.coinsthai.cache.NameCache;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.*;
import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.edm.event.WalletCreateRequestEvent;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.pojo.intenum.WalletLogType;
import com.coinsthai.repository.DealRepository;
import com.coinsthai.repository.WalletLogRepository;
import com.coinsthai.repository.WalletRepository;
import com.coinsthai.service.*;
import com.coinsthai.service.impl.ModelFactory;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.BalanceUpdateRequest;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.wallet.WalletCreateRequest;
import com.coinsthai.vo.wallet.WalletLogParameter;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author
 */
@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletLogRepository logRepository;

    @Autowired
    private PhysicalWalletService physicalWalletService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private NameCache nameCache;

    @Autowired
    private ModelFactory modelFactory;

    @Autowired
    private AuditLogger auditLogger;

    @Transactional
    @Override
    public Wallet create(Wallet wallet) {
        if (wallet.getCoin() == null || StringUtils.isBlank(wallet.getCoin().getId())) {
            BizException ex = new BizException(BizErrorCode.WALLET_COIN_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WALLET, "", ex);
        }
        if (wallet.getUser() == null || StringUtils.isBlank(wallet.getUser().getId())) {
            BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WALLET, "", ex);
        }
        if (StringUtils.isBlank(wallet.getAddress())) {
            BizException ex = new BizException(BizErrorCode.WALLET_ADDRESS_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WALLET, "", ex);
        }
        if (StringUtils.isBlank(wallet.getPrivateKey())) {
            BizException ex = new BizException(BizErrorCode.WALLET_PRIVATE_KEY_EMPTY);
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WALLET, "", ex);
        }

        if (walletRepository.countByAddressAndUserIdNot(wallet.getAddress(), wallet.getUser().getId()) > 0l) {
            BizException ex = new BizException(BizErrorCode.WALLET_ADDRESS_EXISTS,
                                               "address,userId,coinId",
                                               wallet.getAddress(),
                                               wallet.getUser().getId(),
                                               wallet.getCoin().getId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WALLET, wallet.getAddress(), ex);
        }
        if (walletRepository.countByUserIdAndCoinId(wallet.getUser().getId(), wallet.getCoin().getId()) > 0l) {
            BizException ex = new BizException(BizErrorCode.WALLET_EXISTS,
                                               "userId,coinId",
                                               wallet.getUser().getId(),
                                               wallet.getCoin().getId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.WALLET, wallet.getAddress(), ex);
        }

        wallet.setAvailableBalance(0l);
        wallet.setFrozenBalance(0l);
        wallet.setPendingBalance(0l);
        wallet.setCreatedAt(new Date());
        Wallet walletCreated = walletRepository.save(wallet);
        saveLog(walletCreated, 0l, WalletLogType.ADJUST, null, "New Wallet");
        physicalWalletService.create(walletCreated);

        auditLogger.success(AuditAction.CREATED, AuditEntity.WALLET, walletCreated.getId());

        return walletCreated;
    }

    @Override
    public Wallet requestCreate(String userId, String coinId) {
        if (StringUtils.isAnyBlank(userId, coinId)) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        Wallet wallet = getByUserAndCoin(userId, coinId);
        if (wallet != null) {
            return wallet;
        }

        User user = userService.get(userId);
        if (user == null) {
            throw new BizException(BizErrorCode.USER_NOT_FOUND, "id", userId);
        }
        CoinView coin = coinService.get(coinId);
        if (coin == null) {
            throw new BizException(BizErrorCode.COIN_NOT_FOUND, "id", coinId);
        }

        if (!coin.isActive()) {
            if (user.isRobot()) {
                // 创建假钱包
                return createFakeWallet(user, coinId);
            }
            throw new BizException(BizErrorCode.COIN_UNSUPPORTED, "id", coin);
        }

        WalletCreateRequest request = new WalletCreateRequest();
        request.setUserId(userId);
        request.setCoinId(coinId);
        WalletCreateRequestEvent event = new WalletCreateRequestEvent(request);
        eventPublisher.publish(event);
        return null;
    }

    @Override
    public Wallet requestCreateSilently(String userId, String coinId) {
        try {
            return requestCreate(userId, coinId);
        } catch (Exception e) {
            LOGGER.error("Fail to request wallet creation.", e);
            return null;
        }
    }

    @Transactional
    @Override
    public Wallet updateAvailableBalance(BalanceUpdateRequest request) {
        if (request.getType() == null) {
            BizException ex = new BizException(BizErrorCode.BALANCE_LOG_TYPE_EMPTY);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, request.getId(), ex);
        }

        Wallet wallet = getForUpdateAndCheck(request.getId());
        wallet.setAvailableBalance(wallet.getAvailableBalance() + request.getVolume());
        walletRepository.save(wallet);
        saveLog(wallet, request.getVolume(), request.getType(), request.getBizId(), request.getDescription());

        return wallet;
    }

    @Transactional
    @Override
    public Wallet freezeBalance(BalanceUpdateRequest request) {
        Wallet wallet = getForUpdateAndCheck(request.getId());

        long availableBalance = wallet.getAvailableBalance();
        long frozenBalance = wallet.getFrozenBalance();
        long updatingVolume = request.getVolume();
        if (availableBalance < updatingVolume) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "availableBalance,volume",
                                               availableBalance,
                                               updatingVolume);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, request.getId(), ex);
        }

        wallet.setAvailableBalance(availableBalance - updatingVolume);
        wallet.setFrozenBalance(frozenBalance + updatingVolume);
        walletRepository.save(wallet);
        saveLog(wallet, updatingVolume, WalletLogType.FREEZE, request.getBizId(), request.getDescription());

        return wallet;
    }

    @Transactional
    @Override
    public Wallet unfreezeBalance(Bill bill) {
        MarketView market = marketService.get(bill.getMarket().getId());
        Wallet wallet;
        long updatingVolume;

        if (bill.getType() == BillType.SELL) {
            //卖单，解冻目标货币的钱包
            wallet = walletRepository.findByUserIdAndCoinId(bill.getUser().getId(), market.getTargetId());
            updatingVolume = bill.getRemainVolume();
        }
        else {
            //买单，解冻基准币种的钱包
            wallet = walletRepository.findByUserIdAndCoinId(bill.getUser().getId(), market.getBaseId());
            WalletLog walletLog = logRepository.findByWalletIdAndTypeAndBizId(wallet.getId(),
                                                                              WalletLogType.FREEZE,
                                                                              bill.getId());
            if (walletLog == null) {
                // 没有冻结记录，直接返回
                return wallet;
            }

            long paid = findSumByBuy(bill.getId(), market); //已成交的总金额
            updatingVolume = walletLog.getVolume() - paid;
        }

        if (updatingVolume == 0l) {
            return wallet;
        }

        wallet = getForUpdateAndCheck(wallet.getId());
        return unfreezeBalance(wallet, updatingVolume, bill.getId());
    }

    @Transactional
    @Override
    public Wallet unfreezeBalance(Wallet wallet, Bill bill) {
        long updatingVolume;
        if (bill.getType() == BillType.SELL) {
            updatingVolume = bill.getRemainVolume();
        }
        else {
            //买单，解冻基准币种的钱包
            WalletLog walletLog = logRepository.findByWalletIdAndTypeAndBizId(wallet.getId(),
                                                                              WalletLogType.FREEZE,
                                                                              bill.getId());
            if (walletLog == null) {
                // 没有冻结记录，直接返回
                return wallet;
            }

            MarketView market = marketService.get(bill.getMarket().getId());
            long paid = findSumByBuy(bill.getId(), market); //已成交的总金额
            updatingVolume = walletLog.getVolume() - paid;
        }

        if (updatingVolume == 0l) {
            return wallet;
        }

        return unfreezeBalance(wallet, updatingVolume, bill.getId());
    }

    private Wallet unfreezeBalance(Wallet wallet, long updatingVolume, String bizId) {
        long frozenBalance = wallet.getFrozenBalance();
        if (frozenBalance == 0l) {
            auditLogger.fail("unfreeze", AuditEntity.WALLET, wallet.getId(), null);
            return wallet;
        }

        long availableBalance = wallet.getAvailableBalance();
        updatingVolume = NumberUtils.min(updatingVolume, frozenBalance);
        wallet.setAvailableBalance(availableBalance + updatingVolume);
        wallet.setFrozenBalance(frozenBalance - updatingVolume);
        walletRepository.save(wallet);
        saveLog(wallet, updatingVolume, WalletLogType.UNFREEZE, bizId, null);

        return wallet;
    }

    @Transactional
    @Override
    public Wallet unfreezeBalance(Withdraw withdraw) {
        Wallet wallet = getForUpdateAndCheck(withdraw.getWallet().getId());
        long updatingVolume = withdraw.getVolume();
        long availableBalance = wallet.getAvailableBalance();
        long frozenBalance = wallet.getFrozenBalance();
        wallet.setAvailableBalance(availableBalance + updatingVolume);
        wallet.setFrozenBalance(frozenBalance - updatingVolume);
        walletRepository.save(wallet);
        saveLog(wallet, updatingVolume, WalletLogType.UNFREEZE, withdraw.getId(), null);

        return wallet;
    }

    @Transactional
    @Override
    public Wallet updateBalanceForDealOut(BalanceUpdateRequest request) {
        if (request.getType() != WalletLogType.SELL && request.getType() != WalletLogType.BUY) {
            BizException ex = new BizException(BizErrorCode.BALANCE_LOG_TYPE_EMPTY);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, request.getId(), ex);
        }
        Wallet wallet = getForUpdateAndCheck(request.getId());

        long frozenBalance = wallet.getFrozenBalance();
        long availableBalance = wallet.getAvailableBalance();
        long updatingVolume = request.getVolume();
        if (frozenBalance + availableBalance < updatingVolume) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "frozenBalance,availableBalance,volume",
                                               frozenBalance,
                                               availableBalance,
                                               updatingVolume);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, request.getId(), ex);
        }

        if (frozenBalance < updatingVolume) {
            wallet.setAvailableBalance(availableBalance - (updatingVolume - frozenBalance));
            wallet.setFrozenBalance(0l);
        }
        else {
            wallet.setFrozenBalance(frozenBalance - updatingVolume);
        }

        walletRepository.save(wallet);
        saveLog(wallet, 0 - updatingVolume, request.getType(), request.getBizId(), request.getDescription());

        return wallet;
    }

    @Transactional
    @Override
    public Wallet updateBalanceForDealIn(BalanceUpdateRequest request, long brokerage) {
        if (request.getType() != WalletLogType.SELL && request.getType() != WalletLogType.BUY) {
            BizException ex = new BizException(BizErrorCode.BALANCE_LOG_TYPE_EMPTY);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, request.getId(), ex);
        }
        Wallet wallet = getForUpdateAndCheck(request.getId());

        long availableBalance = wallet.getAvailableBalance();
        long updatingVolume = request.getVolume() - brokerage;
        wallet.setAvailableBalance(availableBalance + updatingVolume);
        walletRepository.save(wallet);
        saveLog(wallet, updatingVolume, request.getType(), request.getBizId(), request.getDescription());

        return wallet;
    }

    @Override
    public Wallet updateBalanceForWithdraw(Withdraw withdraw) {
        Wallet wallet = getForUpdateAndCheck(withdraw.getWallet().getId());
        long frozenBalance = wallet.getFrozenBalance();
        long updatingVolume = withdraw.getVolume();
        if (frozenBalance < updatingVolume) {
            BizException ex = new BizException(BizErrorCode.WALLET_BALANCE_EXCEED,
                                               "frozenBalance,volume",
                                               frozenBalance,
                                               updatingVolume);
            auditLogger.fail("withdraw", AuditEntity.WALLET, wallet.getId(), ex);
        }

        wallet.setFrozenBalance(frozenBalance - updatingVolume);
        walletRepository.save(wallet);

        saveLog(wallet, 0 - updatingVolume, WalletLogType.WITHDRAW, withdraw.getId(), null);
        return wallet;
    }

    @Override
    public Wallet rechargePendingBalance(String walletId, long volume, String txHash) {
        if (volume <= 0) {
            BizException ex = new BizException(BizErrorCode.BALANCE_VOLUME_ILLEGAL, "volume", volume);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, walletId, ex);
        }

        Wallet wallet = getForUpdateAndCheck(walletId);
        wallet.setPendingBalance(wallet.getPendingBalance() + volume);
        walletRepository.save(wallet);
        saveLog(wallet, volume, WalletLogType.PRE_RECHARGE, txHash, null);

        return wallet;
    }

    @Override
    public Wallet rechargeAvailableBalance(String walletId, long volume, String txHash) {
        if (volume <= 0) {
            BizException ex = new BizException(BizErrorCode.BALANCE_VOLUME_ILLEGAL, "volume", volume);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, walletId, ex);
        }

        Wallet wallet = getForUpdateAndCheck(walletId);
        wallet.setAvailableBalance(wallet.getAvailableBalance() + volume);
        walletRepository.save(wallet);
        saveLog(wallet, volume, WalletLogType.RECHARGE, txHash, null);

        return wallet;
    }

    @Override
    public Wallet transferPendingBalance(String walletId, long volume, String txHash) {
        if (volume <= 0) {
            BizException ex = new BizException(BizErrorCode.BALANCE_VOLUME_ILLEGAL, "volume", volume);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, walletId, ex);
        }

        Wallet wallet = getForUpdateAndCheck(walletId);
        if (wallet.getPendingBalance() < volume) {
            BizException ex = new BizException(BizErrorCode.BALANCE_VOLUME_ILLEGAL, "volume", volume);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, walletId, ex);
        }

        wallet.setAvailableBalance(wallet.getAvailableBalance() + volume);
        wallet.setPendingBalance(wallet.getPendingBalance() - volume);
        walletRepository.save(wallet);
        saveLog(wallet, volume, WalletLogType.RECHARGE, txHash, null);

        return wallet;
    }

    @Override
    public Wallet get(String id) {
        return walletRepository.findOne(id);
    }

    @Override
    public Wallet getByUserAndCoin(String userId, String coinId) {
        return walletRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public Wallet getByUserAndCoin(String userId, CoinType coinType) {
        String coinId = nameCache.get(coinType.name());
        return getByUserAndCoin(userId, coinId);
    }

    @Override
    public Wallet getByAddressAndCoin(String address, String coinId) {
        return null;
    }

    @Override
    public Wallet getByAddressAndCoin(String address, CoinType coinType) {
        String coinId = nameCache.get(coinType.name());
        return getByAddressAndCoin(address, coinId);
    }

    @Override
    public List<Wallet> listByUser(String userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    public Page<WalletLog> pageLogs(WalletLogParameter parameter) {
        return logRepository.findByPage(parameter);
    }

    private Wallet getForUpdateAndCheck(String id) {
        Wallet wallet = walletRepository.findOneForUpdate(id);
        if (wallet == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_NOT_FOUND);
            auditLogger.fail(AuditAction.UPDATING, AuditEntity.WALLET, id, ex);
        }
        return wallet;
    }

    private WalletLog saveLog(Wallet wallet, long volume, WalletLogType type, String bizId, String description) {
        WalletLog walletLog = new WalletLog();
        walletLog.setWallet(wallet);
        walletLog.setType(type);
        walletLog.setVolume(volume);
        walletLog.setAvailableBalance(wallet.getAvailableBalance());
        walletLog.setFrozenBalance(wallet.getFrozenBalance());
        walletLog.setPendingBalance(wallet.getPendingBalance());
        walletLog.setBizId(bizId);
        walletLog.setDescription(description);
        return logRepository.save(walletLog);
    }

    private long findSumByBuy(String buyId, MarketView market) {
        long sum = 0l;
        List<Deal> deals = dealRepository.findByBuyId(buyId);
        if (deals == null || deals.isEmpty()) {
            return sum;
        }

        CoinView targetCoin = coinService.get(market.getTargetId());
        for (Deal deal : deals) {
            sum += CoinNumberUtils.computeTotalPrice(deal.getPrice(), deal.getVolume(), targetCoin.getUnit());
        }

        return sum;
    }

    // 为机器人创建未开启币种的假钱包
    @Override
    public Wallet createFakeWallet(User user, String coinId) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCoin(modelFactory.getReference(Coin.class, coinId));
        wallet.setAddress("fake" + RandomStringUtils.randomAlphanumeric(24));
        wallet.setPrivateKey("fake" + RandomStringUtils.randomAlphanumeric(24, 32));
        return create(wallet);
    }
}
