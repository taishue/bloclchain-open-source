package com.coinsthai.vo;

import com.coinsthai.pojo.common.BasePojo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author
 */
@ApiModel(description = "市场委托、成交总视图")
public class MarketComplexView extends BasePojo {

    private WalletApiView myTargetWallet;

    private WalletApiView myBaseWallet;

    private List<BillApiView> myPendingBills;

    private List<BillApiView> myFinishedBills;

    private List<MarketTrendApiView> trends;

    private List<BillSimpleApiView> pendingSellBills;

    private List<BillSimpleApiView> pendingBuyBills;

    private List<BillSimpleApiView> finishedDeals;

    @ApiModelProperty("我的市场目标币钱包")
    public WalletApiView getMyTargetWallet() {
        return myTargetWallet;
    }

    public void setMyTargetWallet(WalletApiView myTargetWallet) {
        this.myTargetWallet = myTargetWallet;
    }

    @ApiModelProperty("我的市场基准币钱包")
    public WalletApiView getMyBaseWallet() {
        return myBaseWallet;
    }

    public void setMyBaseWallet(WalletApiView myBaseWallet) {
        this.myBaseWallet = myBaseWallet;
    }

    @ApiModelProperty("我的待处理委托")
    public List<BillApiView> getMyPendingBills() {
        return myPendingBills;
    }

    public void setMyPendingBills(List<BillApiView> myPendingBills) {
        this.myPendingBills = myPendingBills;
    }

    @ApiModelProperty("我的已完成委托")
    public List<BillApiView> getMyFinishedBills() {
        return myFinishedBills;
    }

    public void setMyFinishedBills(List<BillApiView> myFinishedBills) {
        this.myFinishedBills = myFinishedBills;
    }

    @ApiModelProperty("市场动态列表，第一个为当前市场")
    public List<MarketTrendApiView> getTrends() {
        return trends;
    }

    public void setTrends(List<MarketTrendApiView> trends) {
        this.trends = trends;
    }

    @ApiModelProperty("市场中待处理卖单")
    public List<BillSimpleApiView> getPendingSellBills() {
        return pendingSellBills;
    }

    public void setPendingSellBills(List<BillSimpleApiView> pendingSellBills) {
        this.pendingSellBills = pendingSellBills;
    }

    @ApiModelProperty("市场中待处理买单")
    public List<BillSimpleApiView> getPendingBuyBills() {
        return pendingBuyBills;
    }

    public void setPendingBuyBills(List<BillSimpleApiView> pendingBuyBills) {
        this.pendingBuyBills = pendingBuyBills;
    }

    @ApiModelProperty("市场最新成交单")
    public List<BillSimpleApiView> getFinishedDeals() {
        return finishedDeals;
    }

    public void setFinishedDeals(List<BillSimpleApiView> finishedDeals) {
        this.finishedDeals = finishedDeals;
    }
}
