package com.coinsthai.pojo.common;

/**
 * Created by 
 */
public class NamedModifiedAtPojo extends ModifiedAtPojo implements Nameable {
    
    private String name;
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
