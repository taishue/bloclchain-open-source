package com.coinsthai.blockchain;

import com.coinsthai.module.edm.event.WalletCreateRequestEvent;
import com.coinsthai.module.edm.listener.CompositeEventListener;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
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
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author
 */
@Component
public class WalletCreatorFactory extends CompositeEventListener<WalletCreateRequestEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletCreatorFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CoinService coinService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    private Map<CoinType, WalletCreator> creatorMap = new HashMap<>();

    @Override
    public void onEvent(WalletCreateRequestEvent event) {
        WalletCreateRequest request = event.getSource();
        CoinView coin = coinService.get(request.getCoinId());
        if (coin == null) {
            throw new BizException(BizErrorCode.COIN_NOT_FOUND, "id", request.getCoinId());
        }

        Wallet wallet = walletService.getByUserAndCoin(request.getUserId(), request.getCoinId());
        if (wallet != null) {
            // 已经存在，不需要创建
            return;
        }

        CoinType coinType = CoinType.valueOf(coin.getName());
        if (coinType.getTokenOn() != null) {
            // 智能合约
            coinType = coinType.getTokenOn();
        }

        WalletCreator creator = creatorMap.get(coinType);
        if (creator == null) {
            // 判断是否机器，以创建假钱包
            User user = userService.get(request.getUserId());
            if (user.isRobot()) {
                walletService.createFakeWallet(user, request.getCoinId());
                return;
            }

            LOGGER.error("{} is unsupported for creation.", coinType.name());
            return;
            //throw new BizException(BizErrorCode.COIN_UNSUPPORTED, "type", coinType);
        }

        creator.create(request);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();

        Map<String, WalletCreator> beanMap = applicationContext.getBeansOfType(WalletCreator.class);
        beanMap.values().forEach(creator -> creatorMap.put(creator.getCoinType(), creator));
    }

}
