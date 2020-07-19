package com.coinsthai.module.edm;

/**
 * @author
 */
public interface EventListener<T> {

    /**
     * @param event
     */
    void onEvent(T event);

    String getEventName();
}
