package com.coinsthai.bitcoin;

import com.coinsthai.blockchain.WalletCreator;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Coin;
import com.coinsthai.model.User;
import com.coinsthai.model.Wallet;
import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.service.UserService;
import com.coinsthai.service.WalletService;
import com.coinsthai.service.impl.ModelFactory;
import com.coinsthai.util.DateUtils;
import com.coinsthai.vo.wallet.WalletCreateRequest;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.io.File;
import java.security.SecureRandom;
import java.util.Date;

/**
 * @author 
 */
public abstract class BitcoinWalletCreator implements WalletCreator {

    private static final CoinType[] supportedCoins = new CoinType[]{CoinType.BTC, CoinType.BCH};

    @Autowired
    private BitcoinConfig bitcoinConfig;

    @Autowired
    protected WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelFactory modelFactory;

    @Autowired
    private AuditLogger auditLogger;

    @Transactional
    @Override
    public void create(WalletCreateRequest request) {
        Wallet wallet = null;
        Wallet brotherWallet = getBrotherWallet(request.getUserId());
        if (brotherWallet != null) {
            wallet = new Wallet();
            wallet.setUser(brotherWallet.getUser());
            wallet.setCoin(modelFactory.getReference(Coin.class, request.getCoinId()));
            wallet.setPrivateKey(brotherWallet.getPrivateKey());
            wallet.setAddress(brotherWallet.getAddress());
        }
        else {
            wallet = createWallet(request);
        }

        afterWalletCreated(wallet);
    }

    protected void afterWalletCreated(Wallet wallet) {
        // save wallet to db
        walletService.create(wallet);
    }

    private Wallet getBrotherWallet(String userId) {
        for (CoinType type : supportedCoins) {
            if (type == getCoinType()) {
                continue;
            }

            Wallet wallet = walletService.getByUserAndCoin(userId, type);
            if (wallet != null) {
                return wallet;
            }
        }

        return null;
    }

    private Wallet createWallet(WalletCreateRequest request) {
        User user = userService.get(request.getUserId());
        if (user == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "userId", request.getUserId());
            auditLogger.fail(AuditAction.CREATING, "bitcoin wallet", "", ex);
        }

        String day = DateUtils.getDateStr(new Date());
        File file = new File(bitcoinConfig.getWalletPath() + "/" + day);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        String walletName = user.getId() + ".dat";
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            DeterministicSeed deterministicSeed = new DeterministicSeed(secureRandom, 8 * 16,
                                                                        bitcoinConfig.getRandomSalt(),
                                                                        System.currentTimeMillis());

            NetworkParameters params = MainNetParams.get();
            org.bitcoinj.wallet.Wallet wallet = org.bitcoinj.wallet.Wallet.fromSeed(params, deterministicSeed);
            wallet.encrypt(bitcoinConfig.getWalletPassword());

            wallet.saveToFile(new File(file.getPath() + "/" + walletName));
            Address currentReceiveAddress = wallet.currentReceiveAddress();

            Wallet walletModel = new Wallet();
            walletModel.setUser(user);
            walletModel.setPrivateKey(day + "/" + walletName);
            walletModel.setAddress(currentReceiveAddress.toString());
            walletModel.setCoin(modelFactory.getReference(Coin.class, request.getCoinId()));
            return walletModel;
        } catch (Exception e) {
            SystemException ex = new SystemException(SystemException.TYPE.UNKNOWN, e);
            auditLogger.fail(AuditAction.CREATING, "bitcoin wallet", "", ex);
            return null;
        }
    }
}
