package com.coinsthai.module.kyc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class FaceIdConfiguration {

    @Value("${app.kyc.faceid.key}")
    private String appKey;

    @Value("${app.kyc.faceid.secret}")
    private String appSecret;

    @Value("${app.kyc.faceid.url.token}")
    private String tokenUrl;

    @Value("${app.kyc.faceid.url.result}")
    private String resultUrl;

    @Value("${app.kyc.faceid.returnUrl}")
    private String returnUrl;

    @Value("${app.kyc.faceid.notifyUrl}")
    private String notifyUrl;

    @Value("${app.kyc.faceid.webTitle}")
    private String webTitle;

    @Value("${app.kyc.faceid.scene}")
    private String scene;

    @Value("${app.kyc.faceid.procedureType}")
    private String procedureType;

    @Value("${app.kyc.faceid.livenessPreferences}")
    private String livenessPreferences;

    @Value("${app.kyc.faceid.comparisonType}")
    private String comparisonType;

    @Value("${app.kyc.faceid.idcardMode}")
    private String idcardMode;

    @Value("${app.kyc.faceid.idcardUneditableFields}")
    private String idcardUneditableFields;

    @Value("${app.kyc.faceid.multiOrientedDetection}")
    private String multiOrientedDetection;

    @Value("${app.kyc.faceid.returnImage}")
    private String returnImage;

    @Value("${app.kyc.faceid.confidence.threshold}")
    private String confidenceThreshold;

    public String getAppKey() {
        return appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getResultUrl() {
        return resultUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getScene() {
        return scene;
    }

    public String getProcedureType() {
        return procedureType;
    }

    public String getLivenessPreferences() {
        return livenessPreferences;
    }

    public String getComparisonType() {
        return comparisonType;
    }

    public String getIdcardMode() {
        return idcardMode;
    }

    public String getIdcardUneditableFields() {
        return idcardUneditableFields;
    }

    public String getMultiOrientedDetection() {
        return multiOrientedDetection;
    }

    public String getReturnImage() {
        return returnImage;
    }

    public String getConfidenceThreshold() {
        return confidenceThreshold;
    }
}
