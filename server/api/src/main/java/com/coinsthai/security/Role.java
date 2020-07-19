package com.coinsthai.security;

/**
 * Created by
 */
public class Role implements in.clouthink.daas.security.token.core.Role {

    public static final Role ROLE_USER = new Role("USER");

    public static final Role ROLE_ADMIN = new Role("ADMIN");
    
    public Role() {
    }
    
    public Role(String name) {
        this.name = name;
    }
    
    private String name;
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
