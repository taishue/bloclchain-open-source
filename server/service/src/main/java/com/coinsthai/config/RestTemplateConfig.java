package com.coinsthai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author YeYifeng
 */
@Configuration
public class RestTemplateConfig {


    @Value("${app.rest.template.connect.timeout}")
    private int connectTimeout;

    @Value("${app.rest.template.read.timeout}")
    private int readTimeout;

    @Bean
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();

        ClientHttpRequestFactory requestFactory = restTemplate.getRequestFactory();
        if (requestFactory instanceof OkHttp3ClientHttpRequestFactory) {
            OkHttp3ClientHttpRequestFactory okRequestFactory = (OkHttp3ClientHttpRequestFactory) requestFactory;
            okRequestFactory.setConnectTimeout(connectTimeout);
            okRequestFactory.setReadTimeout(readTimeout);
        }

        return restTemplate;
    }
}
