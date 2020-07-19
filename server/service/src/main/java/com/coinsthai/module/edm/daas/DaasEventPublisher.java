package com.coinsthai.module.edm.daas;

import com.coinsthai.module.edm.EventObject;
import com.coinsthai.module.edm.EventPublisher;
import in.clouthink.daas.edm.Edms;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author
 */
@ConditionalOnProperty(name = EventPublisher.MQ_PRODUCER_ENABLE_KEY, havingValue = "false", matchIfMissing = true)
@Service
public class DaasEventPublisher implements EventPublisher {

    @Override
    public void publish(EventObject event) {
        if (event == null || event.getSource() == null) {
            throw new IllegalArgumentException("null source");
        }

        Edms.getEdm().dispatch(event.getClass().getSimpleName(), event);
    }

}
