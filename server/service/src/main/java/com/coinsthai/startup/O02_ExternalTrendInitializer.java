package com.coinsthai.startup;

import com.coinsthai.cache.MarketTrendExternalCache;
import com.coinsthai.service.TrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class O02_ExternalTrendInitializer implements StartupInitializer {

    @Autowired
    private MarketTrendExternalCache externalCache;

    @Autowired
    private TrendService trendService;

    @Override
    public boolean accept() {
        return !externalCache.hasKeys();
    }

    @Override
    public void initialize() {
        trendService.updateExternalTrend();
    }
}
