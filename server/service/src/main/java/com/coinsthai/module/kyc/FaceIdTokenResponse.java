package com.coinsthai.module.kyc;

import com.coinsthai.pojo.common.BasePojo;

/**
 * @author
 */
public class FaceIdTokenResponse extends BasePojo {

    private String userId;

    private String token;

    private String bizId;

    private String requestId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
