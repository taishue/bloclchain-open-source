package com.coinsthai.pojo.common;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * Pojo基类，覆盖了toString() <br/>
 * Created by
 */
public class BasePojo implements Serializable {

    private static final long serialVersionUID = 8022429268603985785L;

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
    
}
