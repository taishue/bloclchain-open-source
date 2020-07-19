package com.coinsthai.module.audit.web;

import com.coinsthai.security.SecurityUser;
import in.clouthink.daas.audit.core.AuditEvent;
import in.clouthink.daas.audit.core.DefaultAuditEvent;
import in.clouthink.daas.audit.spi.AuditEventPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author
 */
public class AuditEventLogger implements AuditEventPersister<DefaultAuditEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditEventLogger.class);

    private static final String FORMAT = "[Time cost: {}ms]{} {} {} from {}.";

    @Override
    public DefaultAuditEvent saveAuditEvent(AuditEvent auditEvent) {
        if (LOGGER.isInfoEnabled()) {
            SecurityUser user = (SecurityUser) auditEvent.getRequestedBy();
            String actor = user == null ? "anonymity" : user.getEmail();
            LOGGER.info(FORMAT,
                        auditEvent.getTimeCost(),
                        actor,
                        auditEvent.getHttpMethod(),
                        auditEvent.getRequestedUrl(),
                        auditEvent.getClientAddress());
        }

        return (DefaultAuditEvent) auditEvent;
    }

}
