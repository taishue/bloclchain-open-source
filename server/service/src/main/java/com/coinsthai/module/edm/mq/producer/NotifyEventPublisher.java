package com.coinsthai.module.edm.mq.producer;

import com.coinsthai.module.edm.EventObject;

/**
 * @author
 */
public interface NotifyEventPublisher {

    void publish(EventObject event);
}
