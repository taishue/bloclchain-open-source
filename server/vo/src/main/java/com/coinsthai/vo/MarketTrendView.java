package com.coinsthai.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "市场趋势")
public class MarketTrendView extends MarketView {

    private long first;

    private long last;

    private long high;

    private long low;

    private long volume;

    private long change;

    @ApiModelProperty("最早价格")
    public long getFirst() {
        return first;
    }

    public void setFirst(long first) {
        this.first = first;
    }

    @ApiModelProperty("最新价格")
    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    @ApiModelProperty("最高价格")
    public long getHigh() {
        return high;
    }

    public void setHigh(long high) {
        this.high = high;
    }

    @ApiModelProperty("最低价格")
    public long getLow() {
        return low;
    }

    public void setLow(long low) {
        this.low = low;
    }

    @ApiModelProperty("成交量")
    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @ApiModelProperty("涨跌幅，万分之几")
    public long getChange() {
        return change;
    }

    public void setChange(long change) {
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

}
