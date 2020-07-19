package com.coinsthai;

import com.coinsthai.module.audit.ActorResolver;
import com.coinsthai.module.audit.DummyActorResolver;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@ComponentScan("com.coinsthai")
@EntityScan("com.coinsthai.model")
@EnableJpaRepositories("com.coinsthai.repository")
@PropertySource(value = {"service.properties", "blockchain.properties", "application.properties"},
        ignoreResourceNotFound = true)
public class TestConfiguration {

    @Bean
    public ActorResolver actorResolver() {
        return new DummyActorResolver();
    }

}
