package com.coinsthai.config;

import com.coinsthai.util.RedisUtils;
import in.clouthink.daas.edm.email.EmailSender;
import in.clouthink.daas.edm.email.impl.EmailSenderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * @author
 */
@Configuration
public class GeneralConfiguration {

    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        return RedisUtils.createRedisTemplate(factory);
    }

    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public EmailSender emailSender(JavaMailSender mailSender) {
        EmailSender emailSender = new EmailSenderImpl(mailSender);
        return emailSender;
    }

}
