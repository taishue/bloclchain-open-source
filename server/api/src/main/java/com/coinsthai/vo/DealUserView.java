package com.coinsthai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "针对单个用户的交易单")
public class DealUserView extends BillSimpleApiView {

    private String marketId;

    private String marketName;

    private String baseName;

    private String targetName;

    private double brokerage;

    @ApiModelProperty("市场ID")
    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    @ApiModelProperty("市场名称")
    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @ApiModelProperty("基准币种名称")
    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    @ApiModelProperty("目标币种名称")
    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @ApiModelProperty("手续费")
    public double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(double brokerage) {
        this.brokerage = brokerage;
    }
}
