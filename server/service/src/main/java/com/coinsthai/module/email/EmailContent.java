package com.coinsthai.module.email;

import java.util.Map;

/**
 * @author
 */
public class EmailContent {

    private String to;

    private String subject;

    /**
     * 邮件正文内容，与template二选一
     */
    private String message;

    /**
     * 邮件正文的模板，与message二选一
     */
    private String template;

    /**
     * 邮件模板内的变量
     */
    private Map<String, ?> context;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Map<String, ?> getContext() {
        return context;
    }

    public void setContext(Map<String, ?> context) {
        this.context = context;
    }
}
