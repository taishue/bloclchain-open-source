package com.coinsthai.vo.wallet;

import com.coinsthai.pojo.intenum.WalletLogType;
import com.coinsthai.pojo.parametric.WalletLogParametric;
import com.coinsthai.vo.DateRangePageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "余额变更查询参数")
public class WalletLogParameter extends DateRangePageParameter implements WalletLogParametric {

    private String userId;

    private String walletId;

    private WalletLogType type;

    @ApiModelProperty("用户ID")
    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ApiModelProperty("钱包ID")
    @Override
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @ApiModelProperty("余额变更类型")
    @Override
    public WalletLogType getType() {
        return type;
    }

    public void setType(WalletLogType type) {
        this.type = type;
    }
}
