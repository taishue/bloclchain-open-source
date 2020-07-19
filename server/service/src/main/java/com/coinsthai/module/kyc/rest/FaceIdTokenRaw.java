package com.coinsthai.module.kyc.rest;

import com.coinsthai.pojo.common.StringIdentifier;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceIdTokenRaw extends StringIdentifier {

    private String token;

    @JsonProperty("request_id")
    private String requestId;

    @JsonProperty("biz_id")
    private String bizId;

    @JsonProperty("expired_time")
    private long expiredTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
