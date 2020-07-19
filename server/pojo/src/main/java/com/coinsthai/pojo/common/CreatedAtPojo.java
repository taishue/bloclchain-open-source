package com.coinsthai.pojo.common;

import java.util.Date;

/**
 * Created by
 */
public class CreatedAtPojo extends StringIdentifier
        implements CreatedAtable {

    private Date createdAt;

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
