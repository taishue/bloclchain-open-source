package com.coinsthai.module.edm.listener;

import com.coinsthai.module.edm.EventListener;
import com.coinsthai.module.edm.daas.AbstractEventListener;

/**
 * @author
 */
public abstract class CompositeEventListener<T> extends AbstractEventListener<T> implements EventListener<T> {


    @Override
    public String getEventName() {
        return super.getEventName();
    }
}
