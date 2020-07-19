package com.coinsthai.service.impl.user;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 */
public enum PasswordEncoder implements IPasswordEncoder {
    
    MD5 {
        @Override
        public String encode(String rawPassword, String salt) {
            Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
            return md5PasswordEncoder.encodePassword(rawPassword, salt);
        }
    }
    
}
