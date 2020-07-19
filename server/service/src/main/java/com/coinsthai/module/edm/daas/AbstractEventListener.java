package com.coinsthai.module.edm.daas;

import in.clouthink.daas.edm.Edms;
import in.clouthink.daas.edm.EventListener;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.ParameterizedType;

/**
 * 自动注册为EventClass.simpleName的监听器<br/>
 * Created by
 */
public abstract class AbstractEventListener<T> implements EventListener<T>, InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        Edms.getEdm().register(getEventName(), this);
    }

    protected String getEventName() {
        Class<T> clazz =
                (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return clazz.getSimpleName();
    }
}
