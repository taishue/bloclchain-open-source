package com.coinsthai.pojo.common;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created by
 */
public abstract class StringIdentifier extends BasePojo implements
                                       StringIdentifiable,
                                       Serializable {
    
    public static final String DEFAULT_ID = "1";
    
    private String id;
    
    @Override
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = StringUtils.isEmpty(id) ? null : id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(getClass())) {
            return false;
        }
        
        StringIdentifier other = (StringIdentifier) obj;
        if (id == null || other.getId() == null) {
            return false;
        }
        
        return id.equals(other.getId());
    }
    
    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        else {
            return id.hashCode();
        }
    }
}
