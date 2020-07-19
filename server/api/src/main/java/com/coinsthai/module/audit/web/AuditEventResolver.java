package com.coinsthai.module.audit.web;

import in.clouthink.daas.audit.core.AuditEventContext;
import in.clouthink.daas.audit.core.MutableAuditEvent;
import in.clouthink.daas.audit.spi.impl.DefaultAuditEventResolver;

/**
 * @author
 */
public class AuditEventResolver extends DefaultAuditEventResolver {

    @Override
    public MutableAuditEvent resolve(AuditEventContext context) {
        return super.resolve(context);
    }

}
