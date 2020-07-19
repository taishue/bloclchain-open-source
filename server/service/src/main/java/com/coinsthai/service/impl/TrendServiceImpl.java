package com.coinsthai.service.impl;

import com.coinsthai.cache.KlineCache;
import com.coinsthai.cache.MarketCache;
import com.coinsthai.cache.MarketTrendCache;
import com.coinsthai.cache.MarketTrendExternalCache;
import com.coinsthai.model.Deal;
import com.coinsthai.model.Kline;
import com.coinsthai.model.Market;
import com.coinsthai.pojo.common.Constant;
import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.repository.DealRepository;
import com.coinsthai.repository.KlineRepository;
import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import com.coinsthai.service.TrendService;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.KlineView;
import com.coinsthai.vo.MarketTrendView;
import com.coinsthai.vo.MarketView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author
 */
@Service
public class TrendServiceImpl implements TrendService {

    @Autowired
    private KlineRepository klineRepository;

    @Autowired
    private DealRepository dealRepository;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private MarketCache marketCache;

    @Autowired
    private MarketTrendCache marketTrendCache;

    @Autowired
    private MarketTrendExternalCache marketTrendExternalCache;

    @Autowired
    private KlineCache klineCache;

    @Autowired
    private KlineViewConverter klineViewConverter;

    @Autowired
    private ModelFactory modelFactory;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app.trend.external.api}")
    private String externalTrendApi;

    @Transactional
    @Override
    public Kline createKline(String marketId, long end, KlineType type) {
        end = fixMilliSeconds(end, type.getMillis());
        long begin = end - type.getMillis();
        Kline kline = createKline(marketId, begin, end, type);

        if (kline == null) {
            return null;
        }

        return klineRepository.save(kline);
    }

    @Transactional
    @Override
    public void initializeKlines(String marketId, KlineType type) {
        if (klineRepository.countByMarketIdAndType(marketId, type) > 0l) {
            // 已经有K线数据
            return;
        }

        long lastEnd = fixMilliSeconds(System.currentTimeMillis(), type.getMillis());
        long firstBegin = lastEnd - type.getMillis() * KlineCache.MAX_LENGTH;
        if (firstBegin < 0l) {
            firstBegin = 0l;
        }
        Kline totalKline = dealRepository.findKline(marketId, new Date(firstBegin), new Date(lastEnd));
        if (totalKline.getVolume() == 0l) {
            // 最近200个K线时间段内无成交记录
            // TODO 考虑200个K线时间段前的交易，以及补全K线记录
            return;
        }

        boolean firstCreated = false;
        for (long begin = firstBegin; begin < lastEnd; begin += type.getMillis()) {
            long end = begin + type.getMillis();
            Kline kline = createKline(marketId, begin, end, type);
            if (kline == null || kline.getVolume() == 0l) {
                continue;
            }
            klineRepository.save(kline);
        }
    }

    @Override
    public void updateKlines(String marketId, Deal deal) {
        for (KlineType type : KlineType.values()) {
            updateKline(marketId, type, deal);
        }
    }

    private void updateKline(String marketId, KlineType type, Deal deal) {
        KlineView view = klineCache.getLatest(marketId, type);
        long mills = deal.getCreatedAt().getTime();
        if (view == null || mills - view.getTimestamp() > type.getMillis()) {
            view = new KlineView();
            view.setMarketId(marketId);
            view.setTimestamp(fixMilliSeconds(mills, type.getMillis()));
            view.setFirst(deal.getPrice());
            view.setLast(deal.getPrice());
            view.setHigh(deal.getPrice());
            view.setLow(deal.getPrice());
            view.setVolume(deal.getVolume());
        }
        else {
            view.setLast(deal.getPrice());
            view.setVolume(view.getVolume() + deal.getVolume());
            if (deal.getPrice() > view.getHigh()) {
                view.setHigh(deal.getPrice());
            }
            if (deal.getPrice() < view.getLow()) {
                view.setLow(deal.getPrice());
            }
        }
        klineCache.add(marketId, type, view);
    }

    @Override
    public List<KlineView> listKlines(String marketId, KlineType type) {
        List<KlineView> list = klineCache.getAll(marketId, type);
        if (list.isEmpty()) {
            List<Kline> models = klineRepository.findTop200ByMarketIdAndTypeOrderByTimestampDesc(marketId, type);
            for (int i = models.size() - 1; i >= 0; i--) {
                Kline model = models.get(i);
                list.add(klineViewConverter.toPojo(model));
            }

            if (!list.isEmpty()) {
                klineCache.set(marketId, type, list);
            }
        }
        return list;
    }

    private Kline createKline(String marketId, long begin, long end, KlineType type) {
        Date beginDate = new Date(begin);
        Date endDate = new Date(end);
        Kline kline = dealRepository.findKline(marketId, beginDate, endDate);
        kline.setTimestamp(begin);
        kline.setType(type);
        kline.setMarket(modelFactory.getReference(Market.class, marketId));
        if (kline.getVolume() == 0l) {
            // 无成交记录，找前一个K线点
            Kline preKline = klineRepository.findTop1ByMarketIdAndTypeAndTimestampLessThanOrderByTimestampDesc(marketId,
                                                                                                               type,
                                                                                                               kline.getTimestamp());
            if (preKline == null) {
                // 如果前面未生成，则本点也不生成
                return null;
            }

            long price = preKline.getLast();
            kline.setLow(price);
            kline.setHigh(price);
            kline.setFirst(price);
            kline.setLast(price);
        }
        else {
            if (kline.getHigh() == kline.getLow()) {
                kline.setFirst(kline.getHigh());
                kline.setLast(kline.getHigh());
            }
            else {
                kline.setFirst(dealRepository.findFirstPrice(marketId, beginDate, endDate));
                kline.setLast(dealRepository.findLastPrice(marketId, beginDate, endDate));
            }
        }

        return kline;
    }

    private Calendar fixEndAndCloneBegin(final Calendar end, int divisor) {
        int minutes = end.get(Calendar.MINUTE);
        int rem = minutes % divisor;
        if (rem != 0) {
            end.add(Calendar.MINUTE, -rem);
        }

        Calendar begin = (Calendar) end.clone();
        begin.add(Calendar.MINUTE, -divisor);
        return begin;
    }

    private long fixMilliSeconds(long ms, long unit) {
        long rem = ms % unit;
        return ms - rem;
    }

    /**
     * 如果未获取到K线，即从市场趋势中获取
     *
     * @param marketId
     * @return
     */
    private long getLatestPriceFromMarket(String marketId) {
        MarketTrendView view = marketTrendCache.get(marketId);
        if (view == null) {
            view = marketTrendExternalCache.get(marketId);
        }

        return view.getLast();
    }

    @Override
    public MarketTrendView updateTrend(MarketView market) {
        MarketTrendView trend = createMarketTrend(market);
        marketTrendCache.set(market.getId(), trend);
        return trend;
    }

    @Override
    public MarketTrendView updateTrend(String marketId, Deal deal) {
        MarketTrendView trend = marketTrendCache.get(marketId);
        if (trend == null) {
            return getTrend(marketId);
        }

        trend.setLast(deal.getPrice());
        trend.setVolume(trend.getVolume() + deal.getVolume());
        if (deal.getPrice() > trend.getHigh()) {
            trend.setHigh(deal.getPrice());
        }
        if (deal.getPrice() < trend.getLow()) {
            trend.setLow(deal.getPrice());
        }

        long change;
        if (trend.getFirst() == 0l) {
            change = 0l;
        }
        else {
            change = (deal.getPrice() - trend.getFirst()) * Constant.CHANGE_RATE_UNIT / trend.getFirst();
        }
        trend.setChange(change);
        marketTrendCache.set(marketId, trend);

        return trend;
    }

    @Override
    public MarketTrendView getTrend(String marketId) {
        MarketTrendView trend = marketTrendCache.get(marketId);
        if (trend == null) {
            MarketView market = marketService.get(marketId);
            trend = createMarketTrend(market);
            marketTrendCache.set(marketId, trend);
        }

        return trend;
    }

    @Override
    public List<MarketTrendView> listHotTrends() {
        List<String> hotIds = marketCache.listHots();
        if (hotIds.isEmpty()) {
            marketService.listAll();    //触发生成hot ids
            hotIds = marketCache.listHots();
        }

        List<MarketTrendView> views = new ArrayList<>(hotIds.size());
        hotIds.forEach(id -> views.add(getTrend(id)));

        return views;
    }

    @Override
    public List<MarketTrendView> listByBaseCoin(String coinId) {
        List<MarketView> markets = marketService.listByBaseCoin(coinId);
        List<MarketTrendView> views = new ArrayList<>(markets.size());
        markets.forEach(market -> views.add(getTrend(market.getId())));

        return views;
    }

    @Override
    public void updateExternalTrend() {
        Map<String, String> map = restTemplate.getForObject(externalTrendApi, Map.class);
        if (map.isEmpty()) {
            return;
        }

        List<MarketView> list = marketService.listAll();
        for (MarketView market : list) {
            String externalName = StringUtils.replace(market.getName(), "/", "");
            String strPrice = map.get(externalName);
            if (StringUtils.isBlank(strPrice)) {
                continue;
            }

            MarketTrendView trendView = marketTrendExternalCache.get(market.getId());
            CoinView baseCoin = coinService.get(market.getBaseId());
            double extPrice = new BigDecimal(strPrice).doubleValue();
            long price = CoinNumberUtils.parseLong(extPrice, baseCoin.getUnit());
            trendView.setFirst(price);
            trendView.setLast(price);
            trendView.setHigh(price);
            trendView.setLow(price);
            marketTrendExternalCache.set(market.getId(), trendView);
        }
    }

    private MarketTrendView createMarketTrend(MarketView market) {
        MarketTrendView trend = new MarketTrendView();
        BeanUtils.copyProperties(market, trend);

        // 最近24小时
        Calendar end = Calendar.getInstance();
        Calendar begin = (Calendar) end.clone();
        begin.add(Calendar.DATE, -1);

        Kline currentDay = dealRepository.findKline(market.getId(), begin.getTime(), end.getTime());
        if (currentDay == null || currentDay.getVolume() == 0) {
            //没有成交，获得最新一分钟的K线
            Kline latestKline = klineRepository.findTop1ByMarketIdAndTypeAndTimestampLessThanOrderByTimestampDesc(market.getId(),
                                                                                                                  KlineType.ONE_MINUTE,
                                                                                                                  end.getTimeInMillis());
            if (latestKline == null) {
                // 如果没有最新K线，返回外部的
                MarketTrendView external = marketTrendExternalCache.get(market.getId());
                trend.setFirst(external.getFirst());
                trend.setLast(external.getLast());
                trend.setHigh(external.getHigh());
                trend.setLow(external.getLow());
                trend.setChange(external.getChange());
            }
            else {
                trend.setVolume(0l);
                trend.setFirst(latestKline.getFirst());
                trend.setLast(latestKline.getLast());
                trend.setHigh(latestKline.getHigh());
                trend.setLow(latestKline.getLow());
                trend.setChange(0);
            }
        }
        else {
            trend.setVolume(currentDay.getVolume());
            trend.setHigh(currentDay.getHigh());
            trend.setLow(currentDay.getLow());

            long last = dealRepository.findLastPrice(market.getId(), begin.getTime(), end.getTime());
            long first = dealRepository.findFirstPrice(market.getId(), begin.getTime(), end.getTime());
            trend.setFirst(first);
            trend.setLast(last);

            long change;
            if (first == 0) {
                change = 0;
            }
            else {
                change = (last - first) * Constant.CHANGE_RATE_UNIT / first;
            }
            trend.setChange(change);
        }

        return trend;
    }


}
