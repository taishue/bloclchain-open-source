package com.coinsthai.service.impl.user;

/**
 * Created by  on 15/12/7.
 */
public interface PasswordValidator {
    
    /**
     * 验证密码是否有效
     * 
     * @param password
     * @return true如果有效，否则false
     */
    boolean isValid(String password);
}
