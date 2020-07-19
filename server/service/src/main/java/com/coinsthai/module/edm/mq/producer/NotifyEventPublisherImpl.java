package com.coinsthai.module.edm.mq.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.module.edm.EventObject;
import com.coinsthai.module.edm.mq.AliyunOnsProducerConfig;
import com.coinsthai.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@ConditionalOnBean(MqEventPublisher.class)
@Component
public class NotifyEventPublisherImpl implements NotifyEventPublisher {

    @Autowired
    private Producer producer;

    @Autowired
    private AliyunOnsProducerConfig config;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    public void publish(EventObject event) {
        String tag = event.getClass().getSimpleName();
        Message message = new Message();
        message.setTopic(config.getNotifyTopic());
        message.setTag(tag);                                //设置为事件名
        message.setShardingKey(event.getClass().getName()); //设置为事件类全名

        String body = JsonUtils.toJson(event);
        message.setBody(body.getBytes());

        // 设置代表消息的业务关键属性，尽可能全局唯一，以方便在无法正常收到消息情况下，可通过 MQ 控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        message.setKey(tag + ":" + event.getKey());

        SendResult result = producer.send(message);
        auditLogger.success("publish", tag, result.getMessageId());
    }
}
