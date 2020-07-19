package com.coinsthai.startup;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.coinsthai.module.edm.mq.AliyunOnsConsumerConfig;
import com.coinsthai.edm.mq.consumer.DealMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author
 */
@Component
public class Z01_StartDealConsumer implements StartupInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Z01_StartDealConsumer.class);

    @Autowired
    private AliyunOnsConsumerConfig config;

    @Autowired
    private DealMessageListener listener;

    @Override
    public boolean accept() {
        return true;
    }

    @Override
    public void initialize() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, config.getId()); // MQ 控制台创建的 Producer ID
        properties.put(PropertyKeyConst.AccessKey, config.getAccessKey());   //鉴权用 AccessKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.SecretKey, config.getSecretKey());  //鉴权用 SecretKey，在阿里云服务器管理控制台创建
        properties.put(PropertyKeyConst.ONSAddr, config.getServer());   // 设置 TCP 接入域名

        OrderConsumer consumer = ONSFactory.createOrderedConsumer(properties);
        consumer.subscribe(config.getTopic(), config.getTag(), listener);
        consumer.start();

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Consumer ID = {}", config.getId());
            LOGGER.info("Topic = {}", config.getTopic());
            LOGGER.info("Tag = {}", config.getTag());
            LOGGER.info("ONS Server = {}", config.getServer());
            LOGGER.info("Deal consumer started.");
        }
    }
}
