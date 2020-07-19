package com.coinsthai.module.edm;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * @author
 */
public abstract class EventObject<T> implements Serializable {

    private T source;

    public EventObject() {
    }


    public EventObject(T source) {
        this.source = source;
    }

    /**
     * 获得事件的关键标识
     * 阿里MQ所用
     *
     * @return
     */
    @JsonIgnore
    public abstract String getKey();

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
