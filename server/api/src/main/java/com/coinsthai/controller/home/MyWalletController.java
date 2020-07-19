package com.coinsthai.controller.home;

import com.coinsthai.controller.BaseController;
import com.coinsthai.converter.ConvertedPage;
import com.coinsthai.converter.WalletLogApiViewConverter;
import com.coinsthai.converter.WalletApiViewConverter;
import com.coinsthai.exception.BizErrorCode;
import com.coinsthai.exception.BizException;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Wallet;
import com.coinsthai.model.WalletLog;
import com.coinsthai.pojo.intenum.WalletLogType;
import com.coinsthai.security.SecurityUser;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.service.WalletService;
import com.coinsthai.util.CoinNumberUtils;
import com.coinsthai.vo.*;
import com.coinsthai.vo.wallet.WalletLogParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
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
@Api(description = "我的钱包API")
@RequestMapping("/api/home/wallets")
@RestController
public class MyWalletController extends BaseController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletApiViewConverter viewConverter;

    @Autowired
    private WalletLogApiViewConverter logViewConverter;

    @ApiOperation(value = "获取当前用户所有钱包")
    @GetMapping()
    public List<WalletApiView> list() {
        List<CoinView> coins = coinService.listActives();
        List<Wallet> models = walletService.listByUser(SecurityUtils.currentUserId());
        List<WalletApiView> views = new ArrayList<>(coins.size());
        coins.forEach(coin -> views.add(getViewByCoin(models, coin)));

        return views;
    }

    @ApiOperation(value = "增加钱包可用余额，测试所用")
    @PostMapping("/balance")
    public WalletApiView updateBalance(@RequestBody BalanceUpdateApiRequest apiRequest) {
        SecurityUser user = SecurityUtils.currentUser();
        if (!user.isRobot()) {
            // 仅限机器人可以调用此API
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        Wallet wallet = walletService.get(apiRequest.getId());
        if (wallet == null) {
            throw new BizException(BizErrorCode.WALLET_NOT_FOUND, "id", apiRequest.getId());
        }
        if (!user.getId().equals(wallet.getUser().getId())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        BalanceUpdateRequest request = new BalanceUpdateRequest();
        BeanUtils.copyProperties(apiRequest, request);
        CoinView coin = coinService.get(wallet.getCoin().getId());
        request.setVolume(CoinNumberUtils.parseLong(apiRequest.getVolume(), coin.getUnit()));
        request.setType(WalletLogType.ADJUST);
        wallet = walletService.updateAvailableBalance(request);

        return viewConverter.toPojo(wallet);
    }

    @ApiOperation(value = "获取指定ID的钱包")
    @GetMapping("/{id}")
    public WalletApiView getById(@ApiParam("钱包ID") @PathVariable String id) {
        id = translateNameToId(id);
        Wallet wallet = walletService.get(id);
        if (!SecurityUtils.currentUserId().equals(wallet.getUser().getId())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        return viewConverter.toPojo(wallet);
    }

    @ApiOperation(value = "获取指定ID的钱包")
    @GetMapping("/by-coin/{coinId}")
    public WalletApiView getByCoin(@ApiParam("钱包ID") @PathVariable String coinId) {
        coinId = translateNameToId(coinId);
        Wallet wallet = walletService.getByUserAndCoin(SecurityUtils.currentUserId(), coinId);
        if (wallet == null) {
            return requestCreate(coinId);
        }

        return viewConverter.toPojo(wallet);
    }

    @ApiOperation(value = "请求创建钱包")
    @PostMapping("/by-coin/{coinId}")
    public WalletApiView requestCreate(@ApiParam("币种ID") @PathVariable String coinId) {
        coinId = translateNameToId(coinId);
        Wallet wallet = walletService.requestCreate(SecurityUtils.currentUserId(), coinId);
        return viewConverter.toPojo(wallet);
    }


    // ================ 余额变更记录 ===============

    @ApiOperation(value = "获取当前钱包的余额变更记录")
    @GetMapping("/{id}/logs")
    public Page<WalletLogApiView> findLogs(@ApiParam("钱包ID") @PathVariable String id,
                                           @ModelAttribute WalletLogParameter parameter) {
        parameter.setWalletId(id);
        return findLogs(parameter);
    }

    @ApiOperation(value = "获取当前钱包的充值记录")
    @GetMapping("/{id}/recharges")
    public Page<WalletLogApiView> findRecharges(@ApiParam("钱包ID") @PathVariable String id,
                                                @ModelAttribute WalletLogParameter parameter) {
        parameter.setWalletId(id);
        parameter.setType(WalletLogType.RECHARGE);
        return findLogs(parameter);
    }

    @ApiOperation(value = "获取当前用户的余额变更记录")
    @GetMapping("/logs")
    public Page<WalletLogApiView> findLogs(@ModelAttribute WalletLogParameter parameter) {
        parameter.setUserId(SecurityUtils.currentUserId());
        Page<WalletLog> modelPage = walletService.pageLogs(parameter);

        Map<String, CoinView> coinMap = new HashMap<>();   //coinId与CoinView
        List<WalletLogApiView> views = new ArrayList<>();
        for (WalletLog model : modelPage) {
            WalletLogApiView view = new WalletLogApiView();
            CoinView coin = getCoin(model.getWallet().getCoin().getId(), coinMap);
            logViewConverter.toPojo(model, view, coin);
            views.add(view);
        }

        return new ConvertedPage<>(modelPage, views);
    }

    /**
     * 根据币种获得对应的钱包VO
     * 如果在模型列表中有，则将模型转成VO，否则生成空的钱包VO
     *
     * @param models 钱包模型列表
     * @param coin   币种
     * @return
     */
    private WalletApiView getViewByCoin(List<Wallet> models, CoinView coin) {
        Wallet match = models.stream()
                             .filter(model -> model.getCoin().getId().equals(coin.getId()))
                             .findFirst()
                             .orElse(null);
        if (match == null) {
            return createEmptyView(coin);
        }
        return viewConverter.toPojo(match);
    }

    private WalletApiView createEmptyView(CoinView coin) {
        WalletApiView view = new WalletApiView();
        view.setUserId(SecurityUtils.currentUserId());
        view.setCoinId(coin.getId());
        view.setCoinName(coin.getName());
        return view;
    }
}
