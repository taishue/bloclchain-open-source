package com.coinsthai.controller.home;

import com.coinsthai.controller.BaseController;
import com.coinsthai.converter.ConvertedPage;
import com.coinsthai.converter.WithdrawApiViewConverter;
import com.coinsthai.converter.WithdrawRequestConverter;
import com.coinsthai.exception.SystemException;
import com.coinsthai.model.Wallet;
import com.coinsthai.model.Withdraw;
import com.coinsthai.security.SecurityUtils;
import com.coinsthai.module.passcode.PasscodeService;
import com.coinsthai.service.WalletService;
import com.coinsthai.service.WithdrawService;
import com.coinsthai.module.passcode.PasscodeRequest;
import com.coinsthai.vo.PasscodeResponse;
import com.coinsthai.vo.WithdrawApiRequest;
import com.coinsthai.vo.WithdrawApiView;
import com.coinsthai.vo.wallet.WithdrawParameter;
import com.coinsthai.vo.wallet.WithdrawRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 
 */
@Api(description = "我的提现API")
@RequestMapping("/api/home/withdraws")
@RestController
public class MyWithdrawController extends BaseController {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private WithdrawRequestConverter requestConverter;

    @Autowired
    private WithdrawApiViewConverter viewConverter;

    @Autowired
    private WalletService walletService;

    @Autowired
    private PasscodeService passcodeService;

    @ApiOperation(value = "获取当前用户的提现记录")
    @GetMapping
    public Page<WithdrawApiView> page(@ModelAttribute WithdrawParameter parameter) {
        parameter.setUserId(SecurityUtils.currentUserId());
        Page<Withdraw> modelPage = withdrawService.page(parameter);
        List<WithdrawApiView> views = new ArrayList<>();
        modelPage.forEach(model -> views.add(viewConverter.toPojo(model)));

        return new ConvertedPage<>(modelPage, views);
    }

    @ApiOperation(value = "申请提现")
    @PostMapping
    public WithdrawApiView apply(@RequestBody WithdrawApiRequest apiRequest) {
        Wallet wallet = walletService.get(apiRequest.getWalletId());
        if (!SecurityUtils.currentUserId().equals(wallet.getUser().getId())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        WithdrawRequest request = requestConverter.toPojo(apiRequest);
        Withdraw withdraw = withdrawService.apply(request);
        return viewConverter.toPojo(withdraw);
    }

    @ApiOperation(value = "获得指定的提现申请")
    @GetMapping("/{id}")
    public WithdrawApiView get(@PathVariable String id) {
        Withdraw withdraw = withdrawService.get(id);
        if (!SecurityUtils.currentUserId().equals(withdraw.getUser().getId())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        return viewConverter.toPojo(withdraw);
    }

    @ApiOperation(value = "撤销提现申请")
    @DeleteMapping("/{id}")
    public WithdrawApiView revoke(@PathVariable String id) {
        Withdraw withdraw = withdrawService.get(id);
        if (!SecurityUtils.currentUserId().equals(withdraw.getUser().getId())) {
            throw new SystemException(SystemException.TYPE.REQUEST_FORBIDDEN_ERROR);
        }

        withdraw = withdrawService.revoke(withdraw);
        return viewConverter.toPojo(withdraw);
    }

    @ApiOperation(value = "发送提现验证码")
    @PostMapping("/passcode")
    public PasscodeResponse sendPasscode() {
        return resendPasscode(null);
    }

    @ApiOperation(value = "重发提现验证码")
    @PostMapping("/passcode/{id}")
    public PasscodeResponse resendPasscode(@PathVariable String id) {
        PasscodeRequest request = new PasscodeRequest();
        request.setId(id);
        request.setReceiverId(SecurityUtils.currentUserId());
        request.setOperation("MSG_PASSCODE_OPERATION_WITHDRAW");
        return passcodeService.request(request);
    }

}
