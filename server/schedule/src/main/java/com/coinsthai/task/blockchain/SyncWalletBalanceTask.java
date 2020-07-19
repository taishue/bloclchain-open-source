package com.coinsthai.task.blockchain;

import com.coinsthai.btc.BtcService;
import com.coinsthai.eth.EthERC20Service;
import com.coinsthai.eth.EthService;
import com.coinsthai.model.Wallet;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.WalletRepository;
import com.coinsthai.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author YeYifeng
 */
@Component
public class SyncWalletBalanceTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncWalletBalanceTask.class);

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private EthService ethService;

    @Autowired
    private EthERC20Service ethERC20Service;

    @Autowired
    private BtcService btcService;

    @Autowired
    private Executor executor;

    @Scheduled(cron = "${app.schedule.eth.sync.balance}")
    public void syncEthWalletBalance() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("ETH balance sync start. time:{}", DateUtils.getDateTimeStr(new Date()));
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Wallet> wallets = walletRepository.findByCoinName(CoinType.ETH.name());
                wallets.forEach(wallet -> {
                    ethService.syncBalance(wallet);
                    ethERC20Service.syncBalance(wallet);
                    Thread current = Thread.currentThread();
                    try {
                        current.sleep(250);
                    } catch (InterruptedException e) {
                        // e.printStackTrace();
                    }
                });
            }
        });
    }

    @Scheduled(cron = "${app.schedule.btc.sync.latest-block}")
    public void syncBtcLatestBlock() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                btcService.syncLatestBlock();
            }
        });
    }

    @Scheduled(cron = "${app.schedule.btc.sync.balance}")
    public void syncBtcWalletBalance() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("BTC balance sync start. time:{}", DateUtils.getDateTimeStr(new Date()));
        }
        List<Wallet> wallets = walletRepository.findByCoinName(CoinType.BTC.name());
        wallets.forEach(wallet -> {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    btcService.syncBalance(wallet);
                }
            });
        });
    }
}
