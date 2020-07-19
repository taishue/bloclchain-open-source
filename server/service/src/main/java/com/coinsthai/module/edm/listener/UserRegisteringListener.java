package com.coinsthai.module.edm.listener;

import com.coinsthai.module.edm.event.UserRegisteringEvent;
import com.coinsthai.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class UserRegisteringListener extends CompositeEventListener<UserRegisteringEvent> {

    @Autowired
    RegisterService registerService;

    @Override
    public void onEvent(UserRegisteringEvent event) {
        if (event == null || StringUtils.isBlank(event.getSource())) {
            return;
        }

        registerService.sendActiveEmail(event.getSource());
    }
}
