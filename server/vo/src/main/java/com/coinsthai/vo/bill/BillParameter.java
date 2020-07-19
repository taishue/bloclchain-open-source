package com.coinsthai.vo.bill;

import com.coinsthai.pojo.intenum.BillStatus;
import com.coinsthai.pojo.intenum.BillType;
import com.coinsthai.pojo.parametric.BillParametric;
import com.coinsthai.vo.DateRangePageParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author 
 */
@ApiModel(description = "委托查询参数")
public class BillParameter extends DateRangePageParameter implements BillParametric {

    private BillType type;

    private BillStatus status;

    private Boolean finished;

    private String userId;

    private String marketId;

    @ApiModelProperty("委托类型")
    @Override
    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    @ApiModelProperty("状态")
    @Override
    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    @ApiModelProperty("是否已结束交易（不包含全部撤单）")
    @Override
    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    @ApiModelProperty("用户ID")
    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
