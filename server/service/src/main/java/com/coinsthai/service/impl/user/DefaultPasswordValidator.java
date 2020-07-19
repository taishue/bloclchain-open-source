package com.coinsthai.service.impl.user;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Created by  on 15/12/7.
 */
@Component
public class DefaultPasswordValidator implements PasswordValidator {
    
    private static final Pattern pattern = Pattern.compile("^(?![A-Z]*$)(?![a-z]*$)(?![0-9]*$)(?![^a-zA-Z0-9]*$)\\S{6,20}$");
    
    @Override
    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        
        return pattern.matcher(password).matches();
    }
}
