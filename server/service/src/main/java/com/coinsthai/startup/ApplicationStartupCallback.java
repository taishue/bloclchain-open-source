package com.coinsthai.startup;

import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class ApplicationStartupCallback implements InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    private static final Log LOGGER =
            LogFactory.getLog(ApplicationStartupCallback.class);

    private List<StartupInitializer> initializers = new ArrayList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.debug("On ContextStartedEvent");
        initializeApplication();
    }

    private void initializeApplication() {
        for (StartupInitializer initializer : initializers) {
            try {
                if (initializer.accept()) {
                    initializer.initialize();
                }
            } catch (Exception e) {
                LOGGER.error("Initialized error.", e);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, StartupInitializer> beanMap = applicationContext.getBeansOfType(StartupInitializer.class);
        List<String> keys = Lists.newArrayList(beanMap.keySet());
        Collections.sort(keys); // 将启动bean按名字排列

        keys.forEach(key -> initializers.add(beanMap.get(key)));
    }
}
