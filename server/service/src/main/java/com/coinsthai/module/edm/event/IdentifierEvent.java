package com.coinsthai.module.edm.event;

import com.coinsthai.module.edm.EventObject;

/**
 * @author
 */
public abstract class IdentifierEvent extends EventObject<String> {

    /**
     * 子类要覆盖此构造函数
     */
    public IdentifierEvent() {
        super();
    }

    public IdentifierEvent(String source) {
        super(source);
    }

    @Override
    public String getKey() {
        return getSource();
    }

}
