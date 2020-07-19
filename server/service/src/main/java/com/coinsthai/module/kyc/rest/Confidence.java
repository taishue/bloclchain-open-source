package com.coinsthai.module.kyc.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Confidence {

    private double confidence;

    private Map<String, Double> thresholds;

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Map<String, Double> getThresholds() {
        return thresholds;
    }

    public void setThresholds(Map<String, Double> thresholds) {
        this.thresholds = thresholds;
    }
}
