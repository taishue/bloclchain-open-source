package com.coinsthai.vo;

import com.coinsthai.pojo.CoinPojo;
import com.coinsthai.pojo.intenum.CoinCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author 
 */
@ApiModel(description = "币种")
public class CoinView extends CoinPojo {

    private String tokenOnId;

    @ApiModelProperty
    public String getTokenOnId() {
        return tokenOnId;
    }

    public void setTokenOnId(String tokenOnId) {
        this.tokenOnId = tokenOnId;
    }

    @ApiModelProperty
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty(value = "简称")
    @Override
    public String getName() {
        return super.getName();
    }

    @ApiModelProperty(value = "创建时间")
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @ApiModelProperty(value = "最后修改时间")
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @ApiModelProperty(value = "全称")
    @Override
    public String getFullName() {
        return super.getFullName();
    }

    @ApiModelProperty(value = "类型，法币/虚拟币")
    @Override
    public CoinCategory getCategory() {
        return super.getCategory();
    }

    @ApiModelProperty(value = "是否可作为基准币")
    @Override
    public boolean isBase() {
        return super.isBase();
    }

    @ApiModelProperty(value = "智能合约地址")
    @Override
    public String getContract() {
        return super.getContract();
    }

    @ApiModelProperty(value = "数量倍数")
    @Override
    public int getUnit() {
        return super.getUnit();
    }

    @ApiModelProperty(value = "最小交易数量")
    @Override
    public long getMinDeal() {
        return super.getMinDeal();
    }

    @ApiModelProperty(value = "最小交易手续费")
    @Override
    public long getMinDealBrokerage() {
        return super.getMinDealBrokerage();
    }

    @ApiModelProperty(value = "最小提现数量")
    @Override
    public long getMinWithdraw() {
        return super.getMinWithdraw();
    }

    @ApiModelProperty(value = "最小提现手续费")
    @Override
    public long getMinNetworkBrokerage() {
        return super.getMinNetworkBrokerage();
    }

    @ApiModelProperty(value = "最大提现手续费")
    @Override
    public long getMaxNetworkBrokerage() {
        return super.getMaxNetworkBrokerage();
    }

    @ApiModelProperty(value = "是否启用")
    @Override
    public boolean isActive() {
        return super.isActive();
    }
}
