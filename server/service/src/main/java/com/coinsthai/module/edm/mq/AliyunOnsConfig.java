package com.coinsthai.module.edm.mq;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 
 */
public abstract class AliyunOnsConfig {

    @Value("${app.aliyun.ons.accessKey}")
    protected String accessKey;

    @Value("${app.aliyun.ons.secretKey}")
    protected String secretKey;

    @Value("${app.aliyun.ons.server}")
    protected String server;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getServer() {
        return server;
    }
}
