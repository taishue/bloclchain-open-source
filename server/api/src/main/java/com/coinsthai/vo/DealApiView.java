package com.coinsthai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "交易单")
public class DealApiView extends BillSimpleApiView {

    private String marketId;

    private String marketName;

    private String sellId;

    private String sellerId;

    private double sellBrokerage;

    private String buyId;

    private String buyerId;

    private double buyBrokerage;

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

    @ApiModelProperty("卖单ID")
    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    @ApiModelProperty("卖家ID")
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @ApiModelProperty("卖单手续费")
    public double getSellBrokerage() {
        return sellBrokerage;
    }

    public void setSellBrokerage(double sellBrokerage) {
        this.sellBrokerage = sellBrokerage;
    }

    @ApiModelProperty("买单ID")
    public String getBuyId() {
        return buyId;
    }

    public void setBuyId(String buyId) {
        this.buyId = buyId;
    }

    @ApiModelProperty("买家ID")
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @ApiModelProperty("买单手续费")
    public double getBuyBrokerage() {
        return buyBrokerage;
    }

    public void setBuyBrokerage(double buyBrokerage) {
        this.buyBrokerage = buyBrokerage;
    }
}
