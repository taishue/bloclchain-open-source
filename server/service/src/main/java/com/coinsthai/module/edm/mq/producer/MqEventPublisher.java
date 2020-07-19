package com.coinsthai.module.edm.mq.producer;

import com.coinsthai.module.edm.EventObject;
import com.coinsthai.module.edm.EventPublisher;
import com.coinsthai.module.edm.event.BillCreationRequestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author 
 */
@ConditionalOnProperty(name = EventPublisher.MQ_PRODUCER_ENABLE_KEY, havingValue = "true")
@Service
public class MqEventPublisher implements EventPublisher {

    @Autowired
    private DealEventPublisher dealEventPublisher;

    @Autowired
    private NotifyEventPublisher notifyEventPublisher;

    @Override
    public void publish(EventObject event) {
        if (event == null || event.getSource() == null) {
            throw new IllegalArgumentException("null source");
        }

        if (event instanceof BillCreationRequestEvent) {
            dealEventPublisher.publish((BillCreationRequestEvent) event);
        }
        else {
            notifyEventPublisher.publish(event);
        }
    }

}
