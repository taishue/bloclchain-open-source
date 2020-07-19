package com.coinsthai.config;

import com.coinsthai.module.audit.web.AuditEventLogger;
import com.coinsthai.module.audit.web.AuditEventResolver;
import in.clouthink.daas.audit.annotation.EnableAudit;
import in.clouthink.daas.audit.configure.AuditConfigurer;
import in.clouthink.daas.audit.core.AuditExecutionConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 */
@Configuration
@EnableAudit
public class WebAuditConfiguration {

    @Bean
    public AuditConfigurer auditConfigurer() {
        AuditEventResolver resolver = new AuditEventResolver();
        AuditEventLogger persister = new AuditEventLogger();

        return new AuditConfigurer() {
            @Override
            public void configure(AuditExecutionConfigurer configurer) {
                configurer.setAuditEventResolver(resolver);
                configurer.setAuditEventPersister(persister);
            }
        };
    }

}
