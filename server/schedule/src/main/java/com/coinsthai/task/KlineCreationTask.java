package com.coinsthai.task;

import com.coinsthai.cache.KlineCache;
import com.coinsthai.model.Kline;
import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.service.MarketService;
import com.coinsthai.service.TrendService;
import com.coinsthai.service.impl.KlineViewConverter;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author 
 */
@Component
public class KlineCreationTask {

    @Autowired
    private TrendService trendService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private KlineViewConverter viewConverter;

    @Autowired
    private KlineCache cache;

    @Autowired
    private Executor executor;


    @Scheduled(cron = "0 * * * * ?")
    public void createOneMinuteKline() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> addTask(market, KlineType.ONE_MINUTE));
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void createFiveMinutesKline() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> addTask(market, KlineType.FIVE_MINUTES));
    }

    @Scheduled(cron = "0 0/15 * * * ?")
    public void createOneQuarterKline() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> addTask(market, KlineType.ONE_QUARTER));
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void createHalfHourKline() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> addTask(market, KlineType.HALF_HOUR));
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void createOneHourKline() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> addTask(market, KlineType.ONE_HOUR));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void createOneDayKline() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> addTask(market, KlineType.ONE_DAY));
    }

    private void addTask(final MarketView market, final KlineType type) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Kline model = trendService.createKline(market.getId(), System.currentTimeMillis(), type);
                cache.add(market.getId(), type, viewConverter.toPojo(model));
            }
        });
    }
}
