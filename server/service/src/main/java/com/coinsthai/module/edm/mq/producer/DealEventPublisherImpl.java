package com.coinsthai.module.edm.mq.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.coinsthai.module.audit.AuditLogger;
import com.coinsthai.module.edm.event.BillCreationRequestEvent;
import com.coinsthai.module.edm.mq.AliyunOnsProducerConfig;
import com.coinsthai.service.MarketService;
import com.coinsthai.util.DateUtils;
import com.coinsthai.util.JsonUtils;
import com.coinsthai.vo.MarketView;
import com.coinsthai.vo.bill.BillCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 
 */
@ConditionalOnBean(MqEventPublisher.class)
@Component
public class DealEventPublisherImpl implements DealEventPublisher {

    @Autowired
    private OrderProducer producer;

    @Autowired
    private AliyunOnsProducerConfig config;

    @Autowired
    private MarketService marketService;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    public void publish(BillCreationRequestEvent event) {
        BillCreateRequest request = event.getSource();
        MarketView market = marketService.get(request.getMarketId());
        String tag = StringUtils.replace(market.getName(), "/", "_");

        Message message = new Message();
        message.setTopic(config.getDealTopic());
        message.setTag(tag);
        message.setShardingKey(tag);

        String body = JsonUtils.toJson(event);
        message.setBody(body.getBytes());

        // 设置代表消息的业务关键属性，尽可能全局唯一，以方便在无法正常收到消息情况下，可通过 MQ 控制台查询消息并补发
        // 注意：不设置也不会影响消息正常收发
        message.setKey(generateKey(request, market));

        SendResult result = producer.send(message, tag);
        auditLogger.success("publish", event.getClass().getSimpleName(), result.getMessageId());
    }

    private String generateKey(BillCreateRequest request, MarketView market) {
        StringBuilder sb = new StringBuilder(market.getName());
        sb.append(" ").append(request.getType().name());
        sb.append(" ").append(request.getVolume()).append("*").append(request.getPrice());
        sb.append(" ").append(request.getUserId());
        sb.append(" ").append(DateUtils.getDateTimeStr(new Date()));
        return sb.toString();
    }
}
