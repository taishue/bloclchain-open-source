package com.coinsthai.controller.home;

import com.coinsthai.controller.BaseController;
import com.coinsthai.vo.BillCreateApiRequest;
import com.coinsthai.converter.BillCreateRequestConverter;
import com.coinsthai.vo.BillApiView;
import com.coinsthai.converter.BillApiFromModelConverter;
import com.coinsthai.converter.ConvertedPage;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Bill;
import com.coinsthai.security.SecurityUser;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.service.BillService;
import com.coinsthai.vo.CoinView;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.PageParameter;
import com.coinsthai.vo.bill.BillCreateRequest;
import com.coinsthai.vo.bill.BillParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Api(description = "我的委托API")
@RequestMapping("/api/home/bills")
@RestController
public class MyBillController extends BaseController {

    @Autowired
    private BillService billService;

    @Autowired
    private BillApiFromModelConverter viewConverter;

    @Autowired
    private BillCreateRequestConverter createRequestConverter;

    @ApiOperation(value = "请求创建委托")
    @PostMapping
    public void requestCreate(@RequestBody BillCreateApiRequest apiRequest) {
        if (apiRequest.getPrice() < 0d || apiRequest.getPrice() == 0d) {
            // 暂时不支付市价交易
            throw new BizException(BizErrorCode.BILL_PRICE_ILLEGAL, "price", apiRequest.getPrice());
        }
        if (apiRequest.getVolume() <=0d) {
            throw new BizException(BizErrorCode.BILL_PRICE_ILLEGAL, "volume", apiRequest.getVolume());
        }
        MarketView market = marketService.get(apiRequest.getMarketId());
        if (market == null) {
            throw new BizException(BizErrorCode.MARKET_NOT_FOUND, "id", apiRequest.getMarketId());
        }

        SecurityUser user = SecurityUtils.currentUser();
        if (!market.isActive() && !user.isRobot()) {
            // 市场关闭的情况下，仅允许机器人进行交易
            throw new BizException(BizErrorCode.BILL_MARKET_CLOSE, "id", apiRequest.getMarketId());
        }

        BillCreateRequest request = createRequestConverter.toPojo(apiRequest);
        request.setUserId(SecurityUtils.currentUserId());

        billService.requestCreate(request);
    }

    @ApiOperation(value = "获取当前用户的委托")
    @GetMapping
    public Page<BillApiView> page(@ModelAttribute BillParameter billParameter) {
        billParameter.setUserId(SecurityUtils.currentUserId());
        Page<Bill> modelPage = billService.page(billParameter);
        Map<String, MarketView> marketMap = new HashMap<>();
        Map<String, CoinView> coinMap = new HashMap<>();    //coinId与CoinView键值对，临时记录以优化查找

        List<BillApiView> views = new ArrayList<>();
        for (Bill model : modelPage) {
            MarketView market = getMarket(model.getMarket().getId(), marketMap);
            CoinView baseCoin = getCoin(market.getBaseId(), coinMap);
            CoinView targetCoin = getCoin(market.getTargetId(), coinMap);
            BillApiView view = new BillApiView();
            view.setMarketId(market.getId());
            view.setMarketName(market.getName());
            viewConverter.toPojo(model, view, market, baseCoin, targetCoin);
            views.add(view);
        }

        return new ConvertedPage<>(modelPage, views);

    }

    @ApiOperation(value = "获取当前用户交易中的委托")
    @GetMapping("/pending")
    public Page<BillApiView> pagePending(@ApiParam("市场ID") @RequestParam String marketId,
                                         @ModelAttribute PageParameter pageParameter) {
        BillParameter billParameter = createBillParameter(marketId, pageParameter);
        billParameter.setFinished(false);

        Page<Bill> modelPage = billService.page(billParameter);
        return viewConverter.toPage(modelPage);
    }

    @ApiOperation(value = "获取当前用户交易完成的委托")
    @GetMapping("/finished")
    public Page<BillApiView> pageFinished(@ApiParam("市场ID") @RequestParam String marketId,
                                          @ModelAttribute PageParameter pageParameter) {
        BillParameter billParameter = new BillParameter();
        billParameter.setPage(pageParameter.getPage());
        billParameter.setSize(pageParameter.getSize());
        billParameter.setMarketId(marketId);
        billParameter.setUserId(SecurityUtils.currentUserId());
        billParameter.setFinished(true);

        Page<Bill> modelPage = billService.page(billParameter);
        return viewConverter.toPage(modelPage);
    }

    @ApiOperation(value = "撤销指定的委托")
    @DeleteMapping("/{id}")
    public void revoke(@PathVariable String id) {
        Bill bill = billService.get(id);
        if (bill == null) {
            throw new BizException(BizErrorCode.BILL_NOT_FOUND, "id", id);
        }

        if (!SecurityUtils.currentUserId().equals(bill.getUser().getId())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        // TODO 是否放到消息队列中处理
        billService.revoke(bill);
    }

    private BillParameter createBillParameter(@ApiParam("市场ID") @RequestParam String marketId,
                                              @ModelAttribute PageParameter pageParameter) {
        BillParameter billParameter = new BillParameter();
        billParameter.setPage(pageParameter.getPage());
        billParameter.setSize(pageParameter.getSize());
        billParameter.setMarketId(marketId);
        billParameter.setUserId(SecurityUtils.currentUserId());
        return billParameter;
    }

}
