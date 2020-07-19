package com.coinsthai.service.impl.user;

/**
 */
public interface IPasswordEncoder {
    
    String encode(String rawPassword, String salt);
    
}
