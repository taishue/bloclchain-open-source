package com.coinsthai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.coinsthai")
@EntityScan("com.coinsthai.model")
@EnableJpaRepositories("com.coinsthai.repository")
@PropertySource(value = {"service.properties", "blockchain.properties", "application.properties"},
        ignoreResourceNotFound = true)
public class DealApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DealApplication.class);
    }

}
