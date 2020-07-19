package com.coinsthai.vo;

import com.coinsthai.pojo.KlinePojo;
import com.coinsthai.pojo.intenum.KlineType;
import io.swagger.annotations.ApiModel;

import java.util.Date;

/**
 * @author
 */
@ApiModel(description = "K线单元")
public class KlineView extends KlinePojo {

    private String marketId;

    @Override
    public KlineType getType() {
        return super.getType();
    }

    @Override
    public long getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    public long getFirst() {
        return super.getFirst();
    }

    @Override
    public long getLast() {
        return super.getLast();
    }

    @Override
    public long getHigh() {
        return super.getHigh();
    }

    @Override
    public long getLow() {
        return super.getLow();
    }

    @Override
    public long getVolume() {
        return super.getVolume();
    }

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    @Override
    public Date getCreatedAt() {
        return super.getCreatedAt();
    }

    @Override
    public Date getModifiedAt() {
        return super.getModifiedAt();
    }

}
