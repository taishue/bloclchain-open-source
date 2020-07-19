package com.coinsthai.startup;

import com.coinsthai.model.User;
import com.coinsthai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by
 */
@Component
public class B01_TestUserInitializer implements StartupInitializer {

    @Autowired
    private UserService userService;

    public static String EMAIL = "test@coinsthai.com";

    @Override
    public boolean accept() {
        return null == userService.getByLogin(EMAIL);
    }

    @Override
    public void initialize() {
        User user = new User();
        user.setEmail(EMAIL);
        user.setPasswordSalt("iYJa5Dy/H8vCYQyIOT76oQ==");
        user.setPassword("coins1@3");
        user.setLocale(Locale.SIMPLIFIED_CHINESE.toString());
        userService.create(user);
    }
}
