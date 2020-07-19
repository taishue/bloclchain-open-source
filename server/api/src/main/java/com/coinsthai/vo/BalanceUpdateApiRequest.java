package com.coinsthai.vo;

import com.coinsthai.pojo.common.StringIdentifier;
import com.coinsthai.pojo.intenum.WalletLogType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "变更余额请求")
public class BalanceUpdateApiRequest extends StringIdentifier {

    private double volume;

    private WalletLogType type;

    private String bizId;

    private String description;

    @ApiModelProperty("钱包ID")
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty("变更量")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @ApiModelProperty("变更类型")
    public WalletLogType getType() {
        return type;
    }

    public void setType(WalletLogType type) {
        this.type = type;
    }

    @ApiModelProperty("关联业务ID")
    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    @ApiModelProperty("描述")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
