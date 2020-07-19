package com.coinsthai.config;

import com.coinsthai.module.audit.ActorResolver;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class NotifyActorResolver implements ActorResolver {

    @Override
    public String resolve() {
        return "Notify-service";
    }
}
