package com.coinsthai.module.edm.listener;

import com.coinsthai.module.edm.event.PasscodeEvent;
import com.coinsthai.module.passcode.PasscodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class PasscodeListener extends CompositeEventListener<PasscodeEvent> {

    @Autowired
    private PasscodeService passcodeService;

    @Override
    public void onEvent(PasscodeEvent event) {
        // 发送邮件
        passcodeService.sendEmail(event.getSource());
    }
}
