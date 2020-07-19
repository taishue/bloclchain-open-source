package com.coinsthai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author
 */
@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
@ComponentScan("com.coinsthai")
@EntityScan("com.coinsthai.model")
@EnableJpaRepositories("com.coinsthai.repository")
@PropertySource(value = {"service.properties", "blockchain.properties", "application.properties"})
public class ScheduleApplication {

    @Value("${app.schedule.maxThreads:100}")
    private int maxThreads = 100;

    @Bean(destroyMethod="shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(maxThreads);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ScheduleApplication.class);
    }

}
