package com.coinsthai.startup;

import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by
 */
@Component
public class O01_MarketCacheInitializer implements StartupInitializer {

    @Autowired
    private CoinService coinService;

    @Autowired
    private MarketService marketService;

    @Override
    public boolean accept() {
        return true;
    }

    @Override
    public void initialize() {
        coinService.listAll();
        marketService.listAll();
    }
}
