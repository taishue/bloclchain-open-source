package com.coinsthai.vo.wallet;

import com.coinsthai.pojo.intenum.WithdrawStatus;
import com.coinsthai.pojo.parametric.WithdrawParametric;
import com.coinsthai.vo.DateRangePageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "提现查询参数")
public class WithdrawParameter extends DateRangePageParameter implements WithdrawParametric {

    private WithdrawStatus status;

    private String userId;

    private String coinId;

    @ApiModelProperty(value = "状态")
    @Override
    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
    }

    @ApiModelProperty(value = "提现人ID")
    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ApiModelProperty(value = "币种ID")
    @Override
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }
}
