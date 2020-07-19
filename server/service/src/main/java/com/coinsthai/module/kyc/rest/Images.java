package com.coinsthai.module.kyc.rest;

import com.coinsthai.pojo.common.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Images extends BasePojo {

    @JsonProperty("image_idcard_back")
    private String idCardBack;

    @JsonProperty("image_idcard_front")
    private String idCardFront;

    @JsonProperty("image_best")
    private String best;

    public String getIdCardBack() {
        return idCardBack;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }
}
