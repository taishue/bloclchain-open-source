package com.coinsthai.module.edm.event;

/**
 * @author 
 */
public class PasswordResetRequestEvent extends IdentifierEvent {

    public PasswordResetRequestEvent() {
    }

    /**
     * @param source 注册邮箱
     */
    public PasswordResetRequestEvent(String source) {
        super(source);
    }
}
