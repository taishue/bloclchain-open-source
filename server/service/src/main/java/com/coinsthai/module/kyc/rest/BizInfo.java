package com.coinsthai.module.kyc.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BizInfo {

    @JsonProperty("biz_extra_data")
    private String bizExtraData;

    @JsonProperty("biz_id")
    private String bizId;

    @JsonProperty("biz_no")
    private String bizNo;

    public String getBizExtraData() {
        return bizExtraData;
    }

    public void setBizExtraData(String bizExtraData) {
        this.bizExtraData = bizExtraData;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }
}
