package com.coinsthai.module.kyc.rest;

import com.coinsthai.pojo.common.BasePojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerifyResult extends BasePojo {

    @JsonProperty("result_faceid")
    private Confidence resultFaceid;

    @JsonProperty("result_ref1")
    private Confidence resultRef1;

    @JsonProperty("result_idcard_photo")
    private Confidence resultIdcardPhoto;

    @JsonProperty("result_idcard_datasource")
    private Confidence resultIdcardDatasource;

    public Confidence getResultFaceid() {
        return resultFaceid;
    }

    public void setResultFaceid(Confidence resultFaceid) {
        this.resultFaceid = resultFaceid;
    }

    public Confidence getResultRef1() {
        return resultRef1;
    }

    public void setResultRef1(Confidence resultRef1) {
        this.resultRef1 = resultRef1;
    }

    public Confidence getResultIdcardPhoto() {
        return resultIdcardPhoto;
    }

    public void setResultIdcardPhoto(Confidence resultIdcardPhoto) {
        this.resultIdcardPhoto = resultIdcardPhoto;
    }

    public Confidence getResultIdcardDatasource() {
        return resultIdcardDatasource;
    }

    public void setResultIdcardDatasource(Confidence resultIdcardDatasource) {
        this.resultIdcardDatasource = resultIdcardDatasource;
    }
}
