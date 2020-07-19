package com.coinsthai.module.audit;

import com.coinsthai.security.SecurityUser;
import com.coinsthai.security.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class ApiActorResolver implements ActorResolver {

    @Override
    public String resolve() {
        SecurityUser user = SecurityUtils.currentUser();
        if (user == null) {
            return "Anonymity";
        }
        return user.getEmail();
    }
}
