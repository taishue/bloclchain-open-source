package com.coinsthai.vo;

import com.coinsthai.pojo.common.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "提现申请")
public class WithdrawApiRequest extends BasePojo {

    private String walletId;

    private String address;

    private double volume;

    private double brokerage;

    private String passcodeId;

    private String passcode;

    @ApiModelProperty("钱包ID")
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

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

    @ApiModelProperty("验证码ID")
    public String getPasscodeId() {
        return passcodeId;
    }

    public void setPasscodeId(String passcodeId) {
        this.passcodeId = passcodeId;
    }

    @ApiModelProperty("验证码")
    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

}
