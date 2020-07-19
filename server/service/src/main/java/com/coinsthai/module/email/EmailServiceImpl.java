package com.coinsthai.module.email;

import com.coinsthai.module.audit.AuditLogger;
import in.clouthink.daas.edm.email.EmailMessage;
import in.clouthink.daas.edm.email.EmailSender;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * @author YeYifeng
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Value("${app.email.sender.name}")
    private String senderName;

    @Value("${app.email.sender.email}")
    private String senderEmail;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private AuditLogger auditLogger;

    @Override
    public void send(EmailContent content) {
        EmailMessage message = new EmailMessage();
        message.setSender(senderName);
        message.setFrom(senderEmail);
        message.setTo(content.getTo());
        message.setSubject(content.getSubject());

        if (StringUtils.isBlank(content.getTemplate())) {
            message.setMessage(content.getMessage());
        }
        else {
            Context context = new Context();
            if (content.getContext() != null) {
                context.setVariables(content.getContext());
            }
            message.setMessage(templateEngine.process(content.getTemplate(), context));
        }

        emailSender.send(message);
        auditLogger.success("send", "email", content.getTo() + " " + content.getSubject());
    }
}
