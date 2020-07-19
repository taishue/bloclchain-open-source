package com.coinsthai.module.audit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
@ConditionalOnMissingBean(value = ActorResolver.class)
public class DummyActorResolver implements ActorResolver {

    @Override
    public String resolve() {
        return "Dummy-system";
    }
}
