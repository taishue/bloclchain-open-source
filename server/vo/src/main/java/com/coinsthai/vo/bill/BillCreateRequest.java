package com.coinsthai.vo.bill;

import com.coinsthai.pojo.common.BasePojo;
import com.coinsthai.pojo.intenum.BillType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author
 */
@ApiModel(description = "挂单请求")
public class BillCreateRequest extends BasePojo {

    private String userId;

    private String marketId;

    private BillType type;

    private long price;

    private long volume;

    @ApiModelProperty(hidden = true)
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

    @ApiModelProperty("挂单类型")
    public BillType getType() {
        return type;
    }

    public void setType(BillType type) {
        this.type = type;
    }

    @ApiModelProperty("价格，如果为市价交易即设为0")
    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @ApiModelProperty("数量")
    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }
}
