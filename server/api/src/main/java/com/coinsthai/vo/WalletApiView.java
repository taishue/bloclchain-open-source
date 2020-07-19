package com.coinsthai.vo;

import com.coinsthai.pojo.intenum.CoinCategory;
import com.coinsthai.pojo.common.StringIdentifier;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "市场")
public class WalletApiView extends StringIdentifier {

    private double availableBalance;

    private double frozenBalance;

    private double pendingBalance;

    private String address;

    private String coinId;

    private String coinName;

    private CoinCategory coinCategory;

    private String userId;

    @ApiModelProperty("可用余额")
    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    @ApiModelProperty("冻结余额")
    public double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    @ApiModelProperty("待确认余额")
    public double getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    @ApiModelProperty("钱包地址")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ApiModelProperty("币ID")
    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    @ApiModelProperty("币种名称")
    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    @ApiModelProperty("币种类型")
    public CoinCategory getCoinCategory() {
        return coinCategory;
    }

    public void setCoinCategory(CoinCategory coinCategory) {
        this.coinCategory = coinCategory;
    }

    @ApiModelProperty("拥有者ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
