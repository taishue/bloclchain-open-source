package com.coinsthai.ltc.impl;

import com.coinsthai.blockchain.WalletCreator;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.ltc.LtcService;
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
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.security.SecureRandom;
import java.util.Date;

/**
 * @author 
 */
@Component
public class LtcWalletCreator implements WalletCreator {

    @Autowired
    private LtcConfig ltcConfig;

    @Autowired
    private LtcService ltcService;

    @Autowired
    protected WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelFactory modelFactory;

    @Autowired
    private AuditLogger auditLogger;


    @Override
    public void create(WalletCreateRequest request) {
        Wallet wallet = createWallet(request);
        walletService.create(wallet);

        ltcService.uploadAddress(request.getUserId(), wallet.getAddress());
    }

    @Override
    public CoinType getCoinType() {
        return CoinType.LTC;
    }

    private Wallet createWallet(WalletCreateRequest request) {
        User user = userService.get(request.getUserId());
        if (user == null) {
            BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "userId", request.getUserId());
            auditLogger.fail(AuditAction.CREATING, "bitcoin wallet", "", ex);
        }

        String day = DateUtils.getDateStr(new Date());
        File file = new File(ltcConfig.getWalletPath() + "/" + day);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }

        String walletName = user.getId() + ".dat";
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            DeterministicSeed deterministicSeed = new DeterministicSeed(secureRandom, 8 * 16,
                                                                        ltcConfig.getRandomSalt(),
                                                                        System.currentTimeMillis());

            NetworkParameters params = LitecoinNetParameters.get();
            org.bitcoinj.wallet.Wallet wallet = org.bitcoinj.wallet.Wallet.fromSeed(params, deterministicSeed);
            wallet.encrypt(ltcConfig.getWalletPassword());

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
            auditLogger.fail(AuditAction.CREATING, "ltc wallet", "", ex);
            return null;
        }
    }

}
