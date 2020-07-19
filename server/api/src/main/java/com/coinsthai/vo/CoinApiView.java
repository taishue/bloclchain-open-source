package com.coinsthai.vo;

import com.coinsthai.pojo.common.NamedModifiedAtPojo;
import com.coinsthai.pojo.intenum.CoinCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "币种")
public class CoinApiView extends NamedModifiedAtPojo {

    private String fullName;

    private CoinCategory category;

    private String contract;

    private boolean base;

    private int unit;

    private double minDeal;

    private double minDealBrokerage;

    private double minWithdraw;

    private double minNetworkBrokerage;

    private double maxNetworkBrokerage;

    private int priority;

    private boolean active;

    @ApiModelProperty(value = "简称")
    @Override
    public String getName() {
        return super.getName();
    }

    @ApiModelProperty(value = "全称")
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @ApiModelProperty(value = "类型，法币/虚拟币")
    public CoinCategory getCategory() {
        return category;
    }

    public void setCategory(CoinCategory category) {
        this.category = category;
    }

    @ApiModelProperty(value = "智能合约地址")
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @ApiModelProperty(value = "是否可作为基准币")
    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    @ApiModelProperty(value = "数量倍数")
    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @ApiModelProperty(value = "最小交易数量")
    public double getMinDeal() {
        return minDeal;
    }

    public void setMinDeal(double minDeal) {
        this.minDeal = minDeal;
    }

    @ApiModelProperty(value = "最小交易手续费")
    public double getMinDealBrokerage() {
        return minDealBrokerage;
    }

    public void setMinDealBrokerage(double minDealBrokerage) {
        this.minDealBrokerage = minDealBrokerage;
    }

    @ApiModelProperty(value = "最小提现数量")
    public double getMinWithdraw() {
        return minWithdraw;
    }

    public void setMinWithdraw(double minWithdraw) {
        this.minWithdraw = minWithdraw;
    }

    @ApiModelProperty(value = "最小提现手续费")
    public double getMinNetworkBrokerage() {
        return minNetworkBrokerage;
    }

    public void setMinNetworkBrokerage(double minNetworkBrokerage) {
        this.minNetworkBrokerage = minNetworkBrokerage;
    }

    @ApiModelProperty(value = "最大提现手续费")
    public double getMaxNetworkBrokerage() {
        return maxNetworkBrokerage;
    }

    public void setMaxNetworkBrokerage(double maxNetworkBrokerage) {
        this.maxNetworkBrokerage = maxNetworkBrokerage;
    }

    @ApiModelProperty(value = "显示顺序，小的在前")
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @ApiModelProperty(value = "是否启用")
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ApiModelProperty(value = "创建时间", hidden = true)
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @ApiModelProperty(value = "最后修改时间", hidden = true)
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

}
