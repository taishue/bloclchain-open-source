package com.coinsthai.pojo.common;

import java.util.Date;

/**
 * Created by
 */
public abstract class ModifiedAtPojo extends CreatedAtPojo
                                         implements ModifiedAtable {
                                         
    private Date modifiedAt;
    
    public Date getModifiedAt() {
        return modifiedAt;
    }
    
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
