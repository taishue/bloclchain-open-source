package com.coinsthai.pojo.common;

/**
 * Created by 
 */
public class NamedPojo extends StringIdentifier implements Nameable {
    
    private String name;
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public static NamedPojo from(Nameable nameable) {
        if (nameable == null) {
            return null;
        }
        
        NamedPojo pojo = new NamedPojo();
        pojo.setId(nameable.getId());
        pojo.setName(nameable.getId());
        return pojo;
    }
}
