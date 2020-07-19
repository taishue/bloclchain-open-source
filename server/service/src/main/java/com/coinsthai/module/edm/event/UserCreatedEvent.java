package com.coinsthai.module.edm.event;

/**
 * 用户已创建事件
 * 用户注册已激活，或由管理员成功创建，会触发此事件
 *
 * @author
 */
public class UserCreatedEvent extends IdentifierEvent {

    public UserCreatedEvent() {
        super();
    }

    public UserCreatedEvent(String source) {
        super(source);
    }
}
