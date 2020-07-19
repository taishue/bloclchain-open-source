package com.coinsthai.vo.wallet;

import com.coinsthai.pojo.common.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "提现申请")
public class WithdrawRequest extends BasePojo {

    private String walletId;

    private String address;

    private long volume;

    private long brokerage;

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

    @ApiModelProperty("网络手续费")
    public long getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(long brokerage) {
        this.brokerage = brokerage;
    }

    @ApiModelProperty("提现数量")
    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
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
