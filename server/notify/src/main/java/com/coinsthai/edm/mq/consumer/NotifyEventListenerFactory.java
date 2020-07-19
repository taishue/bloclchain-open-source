package com.coinsthai.edm.mq.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.coinsthai.module.edm.EventListener;
import com.coinsthai.module.edm.EventObject;
import com.coinsthai.exception.SystemException;
import com.coinsthai.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 */
@Component
public class NotifyEventListenerFactory implements MessageListener, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyEventListenerFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 事件名与监听器列表的映射
     */
    private Map<String, List<EventListener>> listenerMap = new HashMap<>();

    @Override
    public Action consume(Message message, ConsumeContext context) {
        long start = System.currentTimeMillis();

        String json = new String(message.getBody());
        Class eventClass = loadClass(message.getShardingKey());
        EventObject event = (EventObject) JsonUtils.parseJson(json, eventClass);

        boolean errorOccurred = false;
        List<EventListener> listeners = listenerMap.get(message.getTag());
        if (listeners == null || listeners.isEmpty()) {
            LOGGER.warn("No listener for {}.", message.getKey());
        }
        else {
            for (EventListener listener : listeners) {
                try {
                    listener.onEvent(event);
                } catch (Exception e) {
                    errorOccurred = true;
                    LOGGER.error("", e);
                }
            }
            if (!errorOccurred && LOGGER.isInfoEnabled()) {
                LOGGER.info("Notify consumed {} successfully.", message.getKey());
            }
        }

        Action result;
        if (errorOccurred) {
            result = Action.ReconsumeLater;
        }
        result = Action.CommitMessage;

        if (LOGGER.isInfoEnabled()) {
            long end = System.currentTimeMillis();
            String resultStr = errorOccurred ? "failed" : "successfully";
            LOGGER.info("[Time cost: {}ms] Notify consumed {} {}.", end - start, message.getKey(), resultStr);
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventListener> beanMap = applicationContext.getBeansOfType(EventListener.class);
        beanMap.values().forEach(bean -> addToMap(bean));
    }

    private void addToMap(EventListener bean) {
        String eventName = bean.getEventName();
        List<EventListener> list;
        if (listenerMap.containsKey(eventName)) {
            list = listenerMap.get(eventName);
        }
        else {
            list = new ArrayList<>();
            listenerMap.put(eventName, list);
        }

        list.add(bean);
    }

    private Class loadClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new SystemException(SystemException.TYPE.MQ, e);
        }
    }
}
