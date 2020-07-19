package com.coinsthai.module.kyc.rest;

import com.coinsthai.pojo.common.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceIdResultRaw extends BasePojo {

    @JsonProperty("request_id")
    private String requestId;

    private String status;

    @JsonProperty("biz_info")
    private BizInfo bizInfo;

    @JsonProperty("idcard_info")
    private CardInfo cardInfo;

    @JsonProperty("verify_result")
    private VerifyResult verifyResult;

    private Images images;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BizInfo getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(BizInfo bizInfo) {
        this.bizInfo = bizInfo;
    }

    public VerifyResult getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(VerifyResult verifyResult) {
        this.verifyResult = verifyResult;
    }

    public CardInfo getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(CardInfo cardInfo) {
        this.cardInfo = cardInfo;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }
}
