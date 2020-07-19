package com.coinsthai.module.edm.mq.producer;

import com.coinsthai.module.edm.event.BillCreationRequestEvent;

/**
 * @author
 */
public interface DealEventPublisher {

    void publish(BillCreationRequestEvent event);
}
