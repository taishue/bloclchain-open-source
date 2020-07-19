package com.coinsthai.module.edm.mq;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.coinsthai.module.edm.mq.producer.MqEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author
 */
@Component
public class AliyunOnsProducerConfig extends AliyunOnsConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOnsProducerConfig.class);

    @Value("${app.aliyun.ons.topic.deal}")
    private String dealTopic;

    @Value("${app.aliyun.ons.topic.notify}")
    private String notifyTopic;

    @Value("${app.aliyun.ons.producer.deal}")
    private String dealProducer;

    @Value("${app.aliyun.ons.producer.notify}")
    private String notifyProducer;

    @ConditionalOnBean(MqEventPublisher.class)
    @Bean
    public OrderProducer createDealProducer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, dealProducer); // MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.AccessKey, accessKey);   //鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);  //鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.ONSAddr, server);   // 设置 TCP 接入域名
        OrderProducer producer = ONSFactory.createOrderProducer(properties);

        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();
        LOGGER.info("Deal producer started.");

        return producer;
    }

    @ConditionalOnBean(MqEventPublisher.class)
    @Bean
    public Producer createNotifyProducer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, notifyProducer); // MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.AccessKey, accessKey);   //鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, secretKey);  //鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.ONSAddr, server);   // 设置 TCP 接入域名
        Producer producer = ONSFactory.createProducer(properties);

        // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
        producer.start();
        LOGGER.info("Notify producer started.");

        return producer;
    }

    public String getDealProducer() {
        return dealProducer;
    }

    public String getNotifyProducer() {
        return notifyProducer;
    }

    public String getDealTopic() {
        return dealTopic;
    }

    public String getNotifyTopic() {
        return notifyTopic;
    }

}
