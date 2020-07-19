package com.coinsthai.security;

import com.coinsthai.converter.BeanCopyPojoConverter;
import com.coinsthai.model.User;
import org.springframework.stereotype.Component;

/**
 * @author 
 */
@Component
public class SecurityUserConverter extends BeanCopyPojoConverter<SecurityUser, User> {

    @Override
    protected void afterBeanCopy(User source, SecurityUser target) {
        super.afterBeanCopy(source, target);
        target.getRoles().add(Role.ROLE_USER);
        if (source.isRobot()) {
            target.getRoles().add(Role.ROLE_ADMIN);
        }
    }
}
