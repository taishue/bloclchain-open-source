package com.coinsthai.vo;

import com.coinsthai.pojo.common.CreatedAtPojo;
import com.coinsthai.pojo.intenum.BillType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "委托单")
public class BillSimpleApiView extends CreatedAtPojo {

    private double volume;

    private double price;

    private BillType type;

    @ApiModelProperty("数量")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @ApiModelProperty("价格")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @ApiModelProperty("类型")
    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    @ApiModelProperty("成交时间或委托时间")
    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }
}
