package com.coinsthai.converter;

import org.springframework.stereotype.Component;

/**
 * @author
 */
@Component
public class ModelFromViewConverter<T, S> extends AbstractSimpleConverter<T, S> {

    public static String[] IGNORE_FIELDS = new String[]{"id", "createdAt", "modifiedAt"};

    @Override
    protected String[] ignoreExistingWhileUpdating(T target) {
        return IGNORE_FIELDS;
    }

}
