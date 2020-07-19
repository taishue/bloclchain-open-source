package com.coinsthai.module.edm.mq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 消费者的配置
 * 一般一个应用实例只配置一个消费者
 *
 * @author
 */
@Component
public class AliyunOnsConsumerConfig extends AliyunOnsConfig {

    @Value("${app.aliyun.ons.consumer.id}")
    private String id;

    @Value("${app.aliyun.ons.consumer.topic}")
    private String topic;

    @Value("${app.aliyun.ons.consumer.tag}")
    private String tag;

    public String getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }
}
