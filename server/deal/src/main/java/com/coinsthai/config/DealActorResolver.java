package com.coinsthai.config;

import com.coinsthai.module.audit.ActorResolver;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class DealActorResolver implements ActorResolver {

    @Override
    public String resolve() {
        return "Deal-service";
    }
}
