package com.coinsthai.config;

import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author
 */
@Component
public class MessageResolver {

    @Autowired
    private MessageSource resourceBundle;

    public String getMessage(String key) {
        return getMessage(key, (Locale) null);
    }

    public String getMessage(String key, String localeCode) {
        Locale locale = LocaleUtils.toLocale(localeCode);
        return getMessage(key, locale);
    }

    public String getMessage(String key, Locale locale) {
        return resourceBundle.getMessage(key, null, locale);
    }
}
