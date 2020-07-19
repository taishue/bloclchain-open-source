package com.coinsthai.module.edm.event;

/**
 * 用户注册待验证事件
 * 此时用户尚未创建
 *
 * @author
 */
public class UserRegisteringEvent extends IdentifierEvent {

    public UserRegisteringEvent() {
        super();
    }

    /**
     * @param source 注册请求ID，通过此ID从缓存中获得用户注册信息
     */
    public UserRegisteringEvent(String source) {
        super(source);
    }
}
