package com.coinsthai.controller.home;

import com.coinsthai.controller.BaseController;
import com.coinsthai.converter.ConvertedPage;
import com.coinsthai.converter.DealApiViewConverter;
import com.coinsthai.model.Deal;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.service.DealService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.DealApiView;
import com.coinsthai.vo.DealUserView;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.bill.DealParameter;
import com.coinsthai.vo.bill.DealSimpleParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Api(description = "我的交易API")
@RequestMapping("/api/home/deals")
@RestController
public class MyDealController extends BaseController {

    @Autowired
    private DealService dealService;

    @Autowired
    private DealApiViewConverter viewConverter;

    @ApiOperation(value = "获取当前用户的交易")
    @GetMapping
    public Page<DealUserView> page(@ModelAttribute DealSimpleParameter parameter) {
        DealParameter dealParameter = new DealParameter();
        BeanUtils.copyProperties(parameter, dealParameter);
        String currentUserId = SecurityUtils.currentUserId();
        dealParameter.setUserId(currentUserId);

        Page<Deal> modelPage = dealService.page(dealParameter);
        Map<String, MarketView> marketMap = new HashMap<>();
        Map<String, CoinView> coinMap = new HashMap<>();    //coinId与CoinView键值对，临时记录以优化查找

        List<DealUserView> views = new ArrayList<>();
        for (Deal model : modelPage) {
            MarketView market = getMarket(model.getMarket().getId(), marketMap);
            CoinView baseCoin = getCoin(market.getBaseId(), coinMap);
            CoinView targetCoin = getCoin(market.getTargetId(), coinMap);
            DealApiView dealView = new DealApiView();
            dealView.setMarketId(market.getId());
            dealView.setMarketName(market.getName());
            viewConverter.toPojo(model, dealView, market, baseCoin, targetCoin);

            // 如果成交记录买卖双方为同一用户，将会拆分成两条记录
            if (currentUserId.equals(dealView.getSellerId())) {
                DealUserView view = new DealUserView();
                BeanUtils.copyProperties(dealView, view);
                view.setType(BillType.SELL);
                view.setBrokerage(dealView.getSellBrokerage());
                view.setBaseName(baseCoin.getName());
                view.setTargetName(targetCoin.getName());
                views.add(view);
            }
            if (currentUserId.equals(dealView.getBuyerId())) {
                DealUserView view = new DealUserView();
                BeanUtils.copyProperties(dealView, view);
                view.setType(BillType.BUY);
                view.setBrokerage(dealView.getBuyBrokerage());
                view.setBaseName(baseCoin.getName());
                view.setTargetName(targetCoin.getName());
                views.add(view);
            }
        }

        return new ConvertedPage<>(modelPage, views);

    }

}
