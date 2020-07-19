package com.coinsthai.mock;

import com.coinsthai.module.edm.event.WalletCreateRequestEvent;
import com.coinsthai.module.edm.listener.CompositeEventListener;
import com.coinsthai.model.Coin;
import com.coinsthai.model.User;
import com.coinsthai.model.Wallet;
import com.coinsthai.service.WalletService;
import com.coinsthai.service.impl.ModelFactory;
import com.coinsthai.vo.wallet.WalletCreateRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 用于未连接blockchain模块时的测试
 *
 * @author
 */
@ConditionalOnProperty(name = "app.blockchain.enable", havingValue = "false")
@Component
public class WalletCreateRequestListener extends CompositeEventListener<WalletCreateRequestEvent> {

    @Autowired
    private WalletService walletService;

    @Autowired
    private ModelFactory modelFactory;

    @Override
    public void onEvent(WalletCreateRequestEvent event) {
        WalletCreateRequest request = event.getSource();
        if (StringUtils.isAnyBlank(request.getUserId(), request.getCoinId())) {
            return;
        }

        Wallet wallet = walletService.getByUserAndCoin(request.getUserId(), request.getCoinId());
        if (wallet != null) {
            return;
        }

        // save wallet
        wallet = new Wallet();
        wallet.setUser(modelFactory.getReference(User.class, request.getUserId()));
        wallet.setCoin(modelFactory.getReference(Coin.class, request.getCoinId()));
        wallet.setAddress(RandomStringUtils.randomAlphanumeric(24));
        wallet.setPrivateKey(RandomStringUtils.randomAlphanumeric(24, 32));
        walletService.create(wallet);
    }
}
