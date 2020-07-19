package com.coinsthai.vo.bill;

import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.pojo.parametric.DealSimpleParametric;
import com.coinsthai.vo.DateRangePageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "交易查询参数")
public class DealSimpleParameter extends DateRangePageParameter implements DealSimpleParametric {

    private BillType type;

    private String marketId;

    @ApiModelProperty("委托类型")
    @Override
    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    @ApiModelProperty("市场ID")
    @Override
    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

}
