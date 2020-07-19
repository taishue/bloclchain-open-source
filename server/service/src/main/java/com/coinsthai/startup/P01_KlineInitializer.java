package com.coinsthai.startup;

import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.service.MarketService;
import com.coinsthai.service.TrendService;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author
 */
@Component
public class P01_KlineInitializer implements StartupInitializer {

    @Autowired
    private TrendService trendService;

    @Autowired
    private MarketService marketService;

    @Override
    public boolean accept() {
        return true;
    }

    @Override
    public void initialize() {
        List<MarketView> markets = marketService.listAll();
        for (MarketView market : markets) {
            for (KlineType type : KlineType.values()) {
                trendService.initializeKlines(market.getId(), type);
            }
        }
    }
}
