package com.coinsthai.task.blockchain;

import com.coinsthai.bch.BchService;
import com.coinsthai.bch.impl.BchConfig;
import com.coinsthai.model.blockchain.BchTransaction;
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
 * @author
 */
@Component
public class BchUpdateTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(BchUpdateTask.class);

    @Autowired
    private BchService bchService;

    @Autowired
    private BchConfig bchConfig;

    @Autowired
    private Executor executor;

    @Scheduled(cron = "${app.schedule.bch.transaction.sync}")
    public void fetchTransactions() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("BCH balance sync starts at:{}", DateUtils.getDateTimeStr(new Date()));
        }

        executor.execute(() -> bchService.fetchTransactions());
    }

    @Scheduled(cron = "${app.schedule.bch.transaction.confirm}")
    public void confirmTransactions() {
        int maxConfirmations = bchConfig.getMinConfirmations();
        int minConfirmations = maxConfirmations - 3;
        List<BchTransaction> list = bchService.listTransactionsByConfirmations(minConfirmations, maxConfirmations);
        list.forEach(transaction -> executor.execute(() -> bchService.confirmTransaction(transaction)));
    }
}
