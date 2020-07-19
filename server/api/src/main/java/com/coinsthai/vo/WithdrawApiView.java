package com.coinsthai.vo;

import com.coinsthai.pojo.common.StringIdentifier;
import com.coinsthai.pojo.intenum.WithdrawStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "提现记录")
public class WithdrawApiView extends StringIdentifier {

    private String address;

    private double volume;

    private double brokerage;

    private String txid;

    private WithdrawStatus status;

    private String walletId;

    private String userId;

    private String coinId;

    private String coinName;

    private Date createdAt;

    @ApiModelProperty("目标地址")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @ApiModelProperty("提现数量")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @ApiModelProperty("网络手续费")
    public double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(double brokerage) {
        this.brokerage = brokerage;
    }

    @ApiModelProperty("区块链交易ID")
    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    @ApiModelProperty("状态")
    public WithdrawStatus getStatus() {
        return status;
    }

    public void setStatus(WithdrawStatus status) {
        this.status = status;
    }

    @ApiModelProperty("钱包ID")
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    @ApiModelProperty("提现人ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @ApiModelProperty("申请时间")
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
