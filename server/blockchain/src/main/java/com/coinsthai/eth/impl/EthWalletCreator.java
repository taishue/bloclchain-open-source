package com.coinsthai.eth.impl;

import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.blockchain.WalletCreator;
import com.coinsthai.eth.CreateWalletResponse;
import com.coinsthai.eth.EthService;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Coin;
import com.coinsthai.model.User;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.service.CoinService;
import com.coinsthai.service.UserService;
import com.coinsthai.service.WalletService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.wallet.WalletCreateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 *
 * @author
 */
@Component
public class EthWalletCreator implements WalletCreator {

    private final static Logger LOGGER = LoggerFactory.getLogger(EthWalletCreator.class);

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private EthService ethService;

    @Transactional
    @Override
    public void create(WalletCreateRequest request) {
        CoinView coin = coinService.get(request.getCoinId());
        CoinType coinType = CoinType.valueOf(coin.getName());
        CoinView ethCoin = coinService.getByName(CoinType.ETH.name());

        if (CoinType.ETH == coinType) {
            createEthWallet(request.getUserId(), ethCoin);
            return;
        }

        Wallet ethWallet = walletService.getByUserAndCoin(request.getUserId(), ethCoin.getId());
        if (ethWallet != null) {
            // 复制ETH钱包地址来保存代币的钱包
            createWallet(ethWallet.getUser(), ethWallet.getPrivateKey(), ethWallet.getAddress(), coin);
        } else {
            ethWallet = createEthWallet(request.getUserId(), ethCoin);
            // 复制ETH钱包地址来保存代币的钱包
            if (ethWallet != null) {
                createWallet(ethWallet.getUser(), ethWallet.getPrivateKey(), ethWallet.getAddress(), coin);
            }
        }
    }

    @Override
    public CoinType getCoinType() {
        return CoinType.ETH;
    }

    private Wallet createEthWallet(String userId, CoinView coinView) {
        User user = userService.get(userId);
		if (user == null) {
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, "userId", userId);
			auditLogger.fail("create", "create wallet", userId, ex);
		}
		try {
            CreateWalletResponse response = ethService.createWallet();
            return createWallet(user, response.getPrivateKey(), response.getAddress(), coinView);
        } catch (Exception e) {
            LOGGER.error("create ETH wallet failed", e);
			BizException ex = new BizException(BizErrorCode.WALLET_USER_EMPTY, e.getMessage());
			auditLogger.fail("create", "create ETH wallet", userId, ex);
        }
        return null;
    }

    private Wallet createWallet(User user, String privateKey, String address, CoinView coinView) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setPrivateKey(privateKey);
        wallet.setAddress(address);
        Coin coin = new Coin();
        coin.setId(coinView.getId());
        wallet.setCoin(coin);
        walletService.create(wallet);
        return wallet;
    }
}
