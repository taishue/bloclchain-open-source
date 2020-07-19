package com.coinsthai.module.kyc.rest;

import com.coinsthai.pojo.common.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInfo extends BasePojo {

    @JsonProperty("front_side")
    private FrontSide frontSide;

    @JsonProperty("back_side")
    private BackSide backSide;

    public FrontSide getFrontSide() {
        return frontSide;
    }

    public void setFrontSide(FrontSide frontSide) {
        this.frontSide = frontSide;
    }

    public BackSide getBackSide() {
        return backSide;
    }

    public void setBackSide(BackSide backSide) {
        this.backSide = backSide;
    }


}
