package com.coinsthai.security;

import com.coinsthai.model.User;
import com.coinsthai.service.UserService;
import in.clouthink.daas.security.token.spi.IdentityProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 
 */
public class UserIdentityProvider implements IdentityProvider<SecurityUser> {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityUserConverter securityUserConverter;

    @Override
    public SecurityUser findByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }

        String findingUsername = username.toLowerCase().trim();
        User user = userService.getByLogin(findingUsername);
        if (user == null) {
            return null;
        }

        return securityUserConverter.toPojo(user);
    }

}
