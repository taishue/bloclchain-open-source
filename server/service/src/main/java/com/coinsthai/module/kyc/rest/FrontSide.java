package com.coinsthai.module.kyc.rest;

import com.coinsthai.pojo.common.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrontSide extends BasePojo {

    @JsonProperty("ocr_result")
    private CardFrontSide ocrResult;

    @JsonProperty("upload_times")
    private int uploadTimes;

    public CardFrontSide getOcrResult() {
        return ocrResult;
    }

    public void setOcrResult(CardFrontSide ocrResult) {
        this.ocrResult = ocrResult;
    }

    public int getUploadTimes() {
        return uploadTimes;
    }

    public void setUploadTimes(int uploadTimes) {
        this.uploadTimes = uploadTimes;
    }
}
