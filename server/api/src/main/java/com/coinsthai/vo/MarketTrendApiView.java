package com.coinsthai.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "市场趋势")
public class MarketTrendApiView extends MarketView {

    private double first;

    private double last;

    private double high;

    private double low;

    private double volume;

    private double change;

    @ApiModelProperty("最早价格")
    public double getFirst() {
        return first;
    }

    public void setFirst(double first) {
        this.first = first;
    }

    @ApiModelProperty("最新价格")
    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    @ApiModelProperty("最高价格")
    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    @ApiModelProperty("最低价格")
    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @ApiModelProperty("成交量")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @ApiModelProperty("涨跌幅")
    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Override
    public boolean isActive() {
        return super.isActive();
    }
}
