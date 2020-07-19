package com.coinsthai.vo.bill;

import com.coinsthai.pojo.parametric.DealParametric;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "交易查询参数")
public class DealParameter extends DealSimpleParameter implements DealParametric {

    private String userId;

    private String sellerId;

    private String buyerId;

    @ApiModelProperty("买家或卖家ID")
    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ApiModelProperty("卖家ID")
    @Override
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @ApiModelProperty("买家ID")
    @Override
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
}
