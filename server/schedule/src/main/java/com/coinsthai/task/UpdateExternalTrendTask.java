package com.coinsthai.task;

import com.coinsthai.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class UpdateExternalTrendTask {

    @Autowired
    private TrendService trendService;

    @Scheduled(cron = "${app.schedule.trend.external.update}")
    public void start() {
        trendService.updateExternalTrend();
    }
}
