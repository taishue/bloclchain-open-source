package com.coinsthai.vo;

import com.coinsthai.pojo.common.CreatedAtPojo;
import com.coinsthai.pojo.intenum.WalletLogType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author 
 */
@ApiModel(description = "钱包余额变更记录")
public class WalletLogApiView extends CreatedAtPojo {

    private WalletLogType type;

    private double volume;

    private double availableBalance;

    private double frozenBalance;

    private double pendingBalance;

    private String bizId;

    private String description;

    private String walletId;

    private String coinId;

    private String coinName;

    @ApiModelProperty("变更类型")
    public WalletLogType getType() {
        return type;
    }

    public void setType(WalletLogType type) {
        this.type = type;
    }

    @ApiModelProperty("变更的数值")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @ApiModelProperty("变更后的可用余额")
    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    @ApiModelProperty("变更后的冻结余额")
    public double getFrozenBalance() {
        return frozenBalance;
    }

    public void setFrozenBalance(double frozenBalance) {
        this.frozenBalance = frozenBalance;
    }

    @ApiModelProperty("变更后的待确认余额")
    public double getPendingBalance() {
        return pendingBalance;
    }

    public void setPendingBalance(double pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    @ApiModelProperty("关联的业务ID")
    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @ApiModelProperty("说明")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty("币种ID")
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

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @ApiModelProperty("变更时间")
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }
}
