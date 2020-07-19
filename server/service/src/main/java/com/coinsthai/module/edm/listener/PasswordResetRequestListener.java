package com.coinsthai.module.edm.listener;

import com.coinsthai.module.edm.event.PasswordResetRequestEvent;
import com.coinsthai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class PasswordResetRequestListener extends CompositeEventListener<PasswordResetRequestEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onEvent(PasswordResetRequestEvent event) {
        String email = event.getSource();

        // 发送邮件
        userService.sendPasswordResetEmail(email);
    }
}
