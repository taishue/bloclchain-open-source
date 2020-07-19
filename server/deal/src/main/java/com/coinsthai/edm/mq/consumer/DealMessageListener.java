package com.coinsthai.edm.mq.consumer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.coinsthai.module.edm.event.BillCreationRequestEvent;
import com.coinsthai.module.edm.listener.BillCreationRequestListener;
import com.coinsthai.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class DealMessageListener implements MessageOrderListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DealMessageListener.class);

    @Autowired
    private BillCreationRequestListener listener;

    @Override
    public OrderAction consume(Message message, ConsumeOrderContext context) {
        long start = System.currentTimeMillis();

        boolean errorOccurred = false;
        String json = new String(message.getBody());
        BillCreationRequestEvent event = JsonUtils.parseJson(json, BillCreationRequestEvent.class);
        try {
            listener.onEvent(event);
        } catch (Exception e) {
            errorOccurred = true;
            LOGGER.error("Failed to create bill.", e);
        }

        if (LOGGER.isInfoEnabled()) {
            long end = System.currentTimeMillis();
            String resultStr = errorOccurred ? "failed" : "successfully";
            LOGGER.info("[Time cost: {}ms] Notify consumed {} {}.", end - start, message.getKey(), resultStr);
        }

        return OrderAction.Success;
    }
}
