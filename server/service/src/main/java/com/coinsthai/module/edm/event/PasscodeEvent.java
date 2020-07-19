package com.coinsthai.module.edm.event;

/**
 * @author 
 */
public class PasscodeEvent extends IdentifierEvent {

    public PasscodeEvent() {
        super();
    }

    /**
     * @param source 验证码ID，通过此ID从缓存中获得验证码相关信息
     */
    public PasscodeEvent(String source) {
        super(source);
    }

}