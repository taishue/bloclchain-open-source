package com.coinsthai.module.oss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class AliyunOssConfig {

    @Value("${app.aliyun.oss.accessKey}")
    private String accessKey;

    @Value("${app.aliyun.oss.secretKey}")
    private String secretKey;

    @Value("${app.aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${app.aliyun.oss.bucket.private}")
    private String privateBucket;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getPrivateBucket() {
        return privateBucket;
    }
}
