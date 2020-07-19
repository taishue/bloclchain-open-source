package com.coinsthai.module.audit.web;

import com.coinsthai.security.SecurityUser;
import com.coinsthai.security.SecurityUtils;
import in.clouthink.daas.audit.configure.AuditConfigurer;
import in.clouthink.daas.audit.core.AuditExecutionConfigurer;
import in.clouthink.daas.audit.security.SecurityContext;
import org.springframework.context.annotation.Bean;

/**
 * @author
 */
public class AuditSecurityContext implements SecurityContext<SecurityUser> {

    @Override
    public SecurityUser getPrincipal() {
        return SecurityUtils.currentUser();
    }

    @Bean
    public AuditConfigurer auditConfigurer() {
        return new AuditConfigurer() {
            @Override
            public void configure(AuditExecutionConfigurer configurer) {
            }
        };
    }
}
