package com.coinsthai.controller.admin;

import com.coinsthai.converter.ConvertedPage;
import com.coinsthai.converter.WithdrawApiViewConverter;
import com.coinsthai.model.Withdraw;
import com.coinsthai.service.WithdrawService;
import com.coinsthai.vo.WithdrawApiView;
import com.coinsthai.vo.wallet.WithdrawFinishRequest;
import com.coinsthai.vo.wallet.WithdrawParameter;
import com.google.common.collect.Lists;
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
@Api(description = "提现管理API")
@RequestMapping("/api/admin/withdraws")
@RestController
public class WithdrawAdminController {

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private WithdrawApiViewConverter viewConverter;

    @ApiOperation(value = "查询提现记录")
    @GetMapping
    public Page<WithdrawApiView> page(@ModelAttribute WithdrawParameter parameter) {
        if (parameter.getOrders() == null || parameter.getOrders().isEmpty()) {
            parameter.setOrders(Lists.newArrayList("createdAt"));
        }
        Page<Withdraw> modelPage = withdrawService.page(parameter);
        List<WithdrawApiView> views = new ArrayList<>();
        modelPage.forEach(model -> views.add(viewConverter.toPojo(model)));

        return new ConvertedPage<>(modelPage, views);
    }

    @ApiOperation(value = "获得指定的提现申请")
    @GetMapping("/{id}")
    public WithdrawApiView get(@PathVariable String id) {
        Withdraw withdraw = withdrawService.get(id);
        return viewConverter.toPojo(withdraw);
    }

    @ApiOperation(value = "拒绝提现申请")
    @PostMapping("/{id}/decline")
    public WithdrawApiView decline(@PathVariable String id) {
        Withdraw withdraw = withdrawService.decline(id);
        return viewConverter.toPojo(withdraw);
    }

    @ApiOperation(value = "通过提现申请")
    @PostMapping("/{id}/approve")
    public WithdrawApiView approve(@PathVariable String id) {
        Withdraw withdraw = withdrawService.approve(id);
        return viewConverter.toPojo(withdraw);
    }

    @ApiOperation(value = "完成申请提现")
    @PostMapping("/{id}/finish")
    public WithdrawApiView finish(@PathVariable String id, @RequestBody WithdrawFinishRequest request) {
        request.setId(id);
        Withdraw withdraw = withdrawService.finish(request);
        return viewConverter.toPojo(withdraw);
    }

}
