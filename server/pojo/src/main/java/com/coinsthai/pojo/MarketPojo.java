package com.coinsthai.pojo;

import com.coinsthai.pojo.common.NamedModifiedAtPojo;

/**
 * @author
 */
public class MarketPojo extends NamedModifiedAtPojo {

    private boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
