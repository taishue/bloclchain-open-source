package com.coinsthai.vo.bill;

import com.coinsthai.pojo.BillPojo;
import io.swagger.annotations.ApiModel;

/**
 * @author 
 */
@ApiModel(description = "委托单")
public class BillView extends BillPojo {

    private String userId;

    private String marketId;

    private String marketName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }
}
