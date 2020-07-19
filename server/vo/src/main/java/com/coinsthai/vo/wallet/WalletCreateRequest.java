package com.coinsthai.vo.wallet;

import com.coinsthai.pojo.common.BasePojo;

/**
 * @author
 */
public class WalletCreateRequest extends BasePojo {

    private String userId;

    private String coinId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }
}
