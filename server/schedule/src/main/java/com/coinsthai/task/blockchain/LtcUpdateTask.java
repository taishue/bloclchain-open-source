package com.coinsthai.task.blockchain;

import com.coinsthai.ltc.LtcService;
import com.coinsthai.ltc.impl.LtcConfig;
import com.coinsthai.model.blockchain.LtcTransaction;
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
public class LtcUpdateTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(LtcUpdateTask.class);

    @Autowired
    private LtcService ltcService;

    @Autowired
    private LtcConfig ltcConfig;

    @Autowired
    private Executor executor;

    @Scheduled(cron = "${app.schedule.ltc.transaction.sync}")
    public void fetchTransactions() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("LTC balance sync starts at:{}", DateUtils.getDateTimeStr(new Date()));
        }

        executor.execute(() -> ltcService.fetchTransactions());
    }

    @Scheduled(cron = "${app.schedule.ltc.transaction.confirm}")
    public void confirmTransactions() {
        int maxConfirmations = ltcConfig.getMinConfirmations();
        int minConfirmations = maxConfirmations - 3;
        List<LtcTransaction> list = ltcService.listTransactionsByConfirmations(minConfirmations, maxConfirmations);
        list.forEach(transaction -> executor.execute(() -> ltcService.confirmTransaction(transaction)));
    }
}
