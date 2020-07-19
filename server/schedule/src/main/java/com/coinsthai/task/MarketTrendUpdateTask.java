package com.coinsthai.task;

import com.coinsthai.service.MarketService;
import com.coinsthai.service.TrendService;
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
public class MarketTrendUpdateTask {

    @Autowired
    private TrendService trendService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private Executor executor;

    @Scheduled(cron = "${app.schedule.trend.update}")
    public void updateMarketTrends() {
        List<MarketView> markets = marketService.listAll();
        markets.forEach(market -> {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    trendService.updateTrend(market);
                }
            });
        });
    }
}
