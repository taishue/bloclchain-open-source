package com.coinsthai.module.edm;

/**
 * @author
 */
public interface EventPublisher {

    String MQ_PRODUCER_ENABLE_KEY = "app.mq.producer.enable";

    void publish(EventObject event);

}
