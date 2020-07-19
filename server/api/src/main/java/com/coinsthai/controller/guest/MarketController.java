package com.coinsthai.controller.guest;

import com.coinsthai.controller.BaseController;
import com.coinsthai.converter.*;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Deal;
import com.coinsthai.model.Wallet;
import com.coinsthai.model.converter.KlineTypeConverter;
import com.coinsthai.pojo.intenum.KlineType;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.service.BillService;
import com.coinsthai.service.DealService;
import com.coinsthai.service.TrendService;
import com.coinsthai.service.WalletService;
import com.coinsthai.vo.*;
import com.coinsthai.vo.bill.BillSimpleView;
import com.coinsthai.vo.bill.BillView;
import com.coinsthai.vo.bill.DealSimpleParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 */
@Api(description = "市场API")
@RestController
@RequestMapping("/api/markets")
public class MarketController extends BaseController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private BillService billService;

    @Autowired
    private DealService dealService;

    @Autowired
    private TrendService trendService;

    @Autowired
    private MarketTrendApiViewConverter trendApiViewConverter;

    @Autowired
    private WalletApiViewConverter walletViewConverter;

    @Autowired
    private BillApiFromViewConverter billViewConverter;

    @Autowired
    private BillSimpleFromBillConverter billSimpleFromBillConverter;

    @Autowired
    private BillSimpleFromDealConverter billSimpleFromDealConverter;

    @Autowired
    private KlineDataConverter klineDataConverter;

    private KlineTypeConverter klineTypeConverter = new KlineTypeConverter();

    @ApiOperation(value = "获取所有市场")
    @GetMapping()
    public List<MarketView> listAll() {
        return marketService.listActives();
    }

    @ApiOperation(value = "获取指定ID的市场")
    @GetMapping("/{id}")
    public MarketView get(@PathVariable String id) {
        id = translateNameToId(id);
        return marketService.get(id);
    }

    @ApiOperation(value = "获取指定ID的市场趋势")
    @GetMapping("/{id}/trend")
    public MarketTrendApiView getTrend(@PathVariable String id) {
        return trendApiViewConverter.toPojo(trendService.getTrend(id));
    }

    @ApiOperation(value = "获取指定ID的市场最近交易数据")
    @GetMapping("/{id}/summary")
    public MarketComplexView getBills(@ApiParam("市场ID") @PathVariable String id,
                                      @ApiParam("是否显示我的已完成") @RequestParam(required = false) Boolean showMyFinished) {
        id = translateNameToId(id);
        MarketView market = marketService.get(id);
        if (market == null) {
            throw new BizException(BizErrorCode.WALLET_NOT_FOUND, "id", id);
        }

        MarketComplexView result = new MarketComplexView();
        String userId = SecurityUtils.currentUserId();
        if (StringUtils.isNotBlank(userId)) {
            Wallet targetWallet = walletService.getByUserAndCoin(userId, market.getTargetId());
            if (targetWallet == null) {
                walletService.requestCreateSilently(userId, market.getTargetId());
            }
            else {
                result.setMyTargetWallet(walletViewConverter.toPojo(targetWallet));
            }

            Wallet baseWallet = walletService.getByUserAndCoin(userId, market.getBaseId());
            if (baseWallet == null) {
                walletService.requestCreateSilently(userId, market.getBaseId());
            }
            else {
                result.setMyBaseWallet(walletViewConverter.toPojo(baseWallet));
            }

            if (showMyFinished != null && showMyFinished) {
                List<BillView> views = billService.listLatestOfUser(userId, id, showMyFinished);
                result.setMyFinishedBills(billViewConverter.toList(views));
            }
            else {
                List<BillView> views = billService.listLatestOfUser(userId, id, false);
                result.setMyPendingBills(billViewConverter.toList(views));
            }
        }

        // 市场动态
        List<MarketTrendApiView> trends = new ArrayList<>();
        trends.add(trendApiViewConverter.toPojo(trendService.getTrend(id)));
        List<MarketTrendView> hotTrends = trendService.listHotTrends();
        for (MarketTrendView view : hotTrends) {
            if (!id.equals(view.getId())) {
                trends.add(trendApiViewConverter.toPojo(view));
            }
            if (trends.size() >= 3) {
                break;
            }
        }
        result.setTrends(trends);

        // 获取价格最低的卖单
        List<BillSimpleView> sells = billService.listLowestPendingSells(id);
        BillSimpleFromViewConverter simpleConverter = new BillSimpleFromViewConverter(id, marketService, coinService);
        result.setPendingSellBills(simpleConverter.toList(sells));

        // 获取价格最高的买单
        List<BillSimpleView> buys = billService.listHighestPendingBuys(id);
        result.setPendingBuyBills(simpleConverter.toList(buys));

        // 获取最新的成交单
        DealSimpleParameter dealParameter = new DealSimpleParameter();
        dealParameter.setMarketId(id);
        dealParameter.setSize(50);
        Page<Deal> dealPage = dealService.pageSimple(dealParameter);
        result.setFinishedDeals(billSimpleFromDealConverter.toList(dealPage.getContent()));

        return result;
    }

    @ApiOperation(value = "根据基准币种ID获得市场动态列表")
    @GetMapping("/trends/{baseCoinId}")
    public List<MarketTrendApiView> listTrendsByBase(@Param("市场基准货币ID") @PathVariable String baseCoinId) {
        baseCoinId = translateNameToId(baseCoinId);
        List<MarketTrendView> views = trendService.listByBaseCoin(baseCoinId);
        List<MarketTrendView> actives = views.stream().filter(view -> view.isActive()).collect(Collectors.toList());
        return trendApiViewConverter.toList(actives);
    }

    @ApiOperation(value = "获得指定市场及类型的K线")
    @GetMapping("/{id}/klines/{type}")
    public KlinesWrapper getKlines(@PathVariable String id, @PathVariable int type) {
        id = translateNameToId(id);
        KlineType klineType = klineTypeConverter.convertToEntityAttribute(type);
        if (klineType == null) {
            throw new BizException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        KlinesWrapper wrapper = new KlinesWrapper();
        KlinesView klinesView = new KlinesView();
        wrapper.setDatas(klinesView);

        List<KlineView> views = trendService.listKlines(id, klineType);
        klinesView.setData(klineDataConverter.toList(views));

        return wrapper;
    }

}
