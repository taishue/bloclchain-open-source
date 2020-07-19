package com.coinsthai.vo;

import com.coinsthai.pojo.intenum.BillStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "委托单")
public class BillApiView extends BillSimpleApiView {

    private double remainVolume;

    private double dealValue;

    private double averagePrice;

    private double brokerageRate;

    private double brokerage;

    private BillStatus status;

    private String userId;

    private String marketId;

    private String marketName;

    @ApiModelProperty
    @Override
    public String getId() {
        return super.getId();
    }

    @ApiModelProperty("拥有者ID")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    @ApiModelProperty("挂单总数")
    @Override
    public double getVolume() {
        return super.getVolume();
    }

    @ApiModelProperty("未成交数量")
    public double getRemainVolume() {
        return remainVolume;
    }

    public void setRemainVolume(double remainVolume) {
        this.remainVolume = remainVolume;
    }

    @ApiModelProperty("价格，如果是市价，即为0")
    @Override
    public double getPrice() {
        return super.getPrice();
    }

    @ApiModelProperty("成交均价，状态Pending时为0")
    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    @ApiModelProperty("成交总额")
    public double getDealValue() {
        return dealValue;
    }

    public void setDealValue(double dealValue) {
        this.dealValue = dealValue;
    }

    @ApiModelProperty("手续费率")
    public double getBrokerageRate() {
        return brokerageRate;
    }

    public void setBrokerageRate(double brokerageRate) {
        this.brokerageRate = brokerageRate;
    }

    @ApiModelProperty("手续费")
    public double getBrokerage() {
        return brokerage;
    }

    public void setBrokerage(double brokerage) {
        this.brokerage = brokerage;
    }

    @ApiModelProperty("状态")
    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }


}
