package com.coinsthai.service.impl;

import com.coinsthai.module.audit.AuditAction;
import com.coinsthai.module.audit.AuditEntity;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.cache.MarketCache;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.model.Market;
import com.coinsthai.pojo.blockchain.CoinType;
import com.coinsthai.repository.MarketRepository;
import com.coinsthai.service.CoinService;
import com.coinsthai.service.MarketService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author
 */
@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private MarketCache marketCache;

    @Autowired
    private MarketConverter modelConverter;

    @Autowired
    private MarketViewConverter viewConverter;

    @Autowired
    private CoinService coinService;

    @Autowired
    private AuditLogger auditLogger;

    @Transactional
    @Override
    public MarketView create(MarketView view) {
        // TODO check
        if (marketRepository.findByCoins(view.getTargetId(), view.getBaseId()) != null) {
            BizException ex = new BizException(BizErrorCode.MARKET_EXISTS,
                                               "targetId,baseId",
                                               view.getTargetId(),
                                               view.getBaseId());
            auditLogger.fail(AuditAction.CREATING, AuditEntity.MARKET, "", ex);
        }

        Market model = modelConverter.toPojo(view);
        model.setActive(true);
        marketRepository.save(model);
        auditLogger.success(AuditAction.CREATED, AuditEntity.MARKET, model.getId());

        MarketView createdView = viewConverter.toPojo(model);
        marketCache.set(createdView.getId(), createdView);

        return createdView;
    }

    @Override
    public MarketView get(String id) {
        MarketView view = marketCache.get(id);
        if (view == null) {
            Market model = marketRepository.findOne(id);
            if (model == null) {
                BizException ex = new BizException(BizErrorCode.MARKET_NOT_FOUND, "id", id);
                auditLogger.fail(AuditAction.VIEWING, AuditEntity.MARKET, id, ex);
            }

            view = viewConverter.toPojo(model);
            marketCache.set(id, view);
        }

        return view;
    }

    @Override
    public List<MarketView> listAll() {
        List<MarketView> views = marketCache.listAll();
        if (views.isEmpty()) {
            List<Market> models = marketRepository.findAllByOrderByBasePriorityAscTargetPriorityAsc();

            List<String> ids = new ArrayList<>(models.size());
            List<String> hots = new ArrayList<>();
            Map<String, List<String>> groupedMap = createBaseGroupedMap();

            for (Market model : models) {
                MarketView view = viewConverter.toPojo(model);
                views.add(view);
                ids.add(view.getId());
                if (isHot(view)) {
                    hots.add(view.getId());
                }

                if (groupedMap.containsKey(view.getBaseId())) {
                    groupedMap.get(view.getBaseId()).add(view.getId());
                }
                marketCache.set(view.getId(), view);
            }

            marketCache.setAll(ids);
            marketCache.setHots(hots);
            for (String coinId : groupedMap.keySet()) {
                List<String> marketIds = groupedMap.get(coinId);
                if (!marketIds.isEmpty()) {
                    marketCache.setBaseGrouped(coinId, marketIds);
                }
            }
        }

        return views;
    }

    @Override
    public List<MarketView> listActives() {
        List<MarketView> all = listAll();
        return all.stream().filter(view -> view.isActive()).collect(Collectors.toList());
    }

    @Override
    public List<MarketView> listByBaseCoin(String coinId) {
        List<MarketView> views = marketCache.listBasedGroupedMarkets(coinId);
        if (views != null && !views.isEmpty()) {
            return views;
        }

        // 从数据库中获取，并保存到缓存
        List<Market> markets = marketRepository.findByBaseIdOrderByTargetPriorityAsc(coinId);
        List<String> marketIds = new ArrayList<>(markets.size());
        markets.forEach(market -> marketIds.add(market.getId()));
        marketCache.setBaseGrouped(coinId, marketIds);

        return viewConverter.toList(markets);
    }

    private boolean isHot(MarketView view) {
        return isHotCoin(view.getBaseName()) && isHotCoin(view.getTargetName());
    }

    private boolean isHotCoin(String coinName) {
        return CoinType.BTC.name().equalsIgnoreCase(coinName) ||
                CoinType.ETH.name().equalsIgnoreCase(coinName) ||
                CoinType.ETC.name().equalsIgnoreCase(coinName) ||
                CoinType.XRP.name().equalsIgnoreCase(coinName);
    }

    private Map<String, List<String>> createBaseGroupedMap() {
        Map<String, List<String>> map = new HashMap<>();
        List<CoinView> coins = coinService.listAll();
        for (CoinView coin : coins) {
            if (coin.isBase()) {
                map.put(coin.getId(), new ArrayList<>());
            }
        }

        return map;
    }

}
