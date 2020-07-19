package com.coinsthai.pojo.common;

/**
 * Created by
 */
public class NamedCreatedAtPojo extends CreatedAtPojo implements Nameable {
    
    private String name;
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
