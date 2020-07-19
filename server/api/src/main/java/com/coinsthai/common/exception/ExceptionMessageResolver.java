package com.coinsthai.common.exception;

import com.coinsthai.exception.BizException;
import com.coinsthai.exception.DefaultErrorCode;
import com.coinsthai.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Optional;

@Component
public class ExceptionMessageResolver {

    @Autowired
    private MessageSource resourceBundle;


    public String getErrorMessage(BizException exception) {
        return getErrorMessage(exception.getErrorCode(), exception.getArguments());
    }

    public String getErrorMessage(ConstraintViolationException exception) {
        Optional<ConstraintViolation<?>> optional = exception.getConstraintViolations().stream().findFirst();
        if (optional.isPresent()) {
            return getErrorMessage(optional.get().getMessageTemplate(), null);
        }

        return getErrorMessage(DefaultErrorCode.UNEXPECTED_ERROR, null);
    }

    public String getErrorMessage(Throwable exception) {
        BizException bizException = getCauseException(exception, BizException.class);
        if (bizException != null) {
            return getErrorMessage(bizException);
        }

        ConstraintViolationException constraintViolationException = getCauseException(exception,
                                                                                      ConstraintViolationException.class);
        if (constraintViolationException != null) {
            return getErrorMessage(constraintViolationException);
        }

        return getErrorMessage(DefaultErrorCode.UNEXPECTED_ERROR, null);
    }

    private String getErrorMessage(ErrorCode errorCode, Object[] args) {
        return resourceBundle.getMessage(errorCode.toString(),
                                         args,
                                         errorCode.toString(),
                                         resolveLocale());
    }

    private String getErrorMessage(String code, Object[] args) {
        return resourceBundle.getMessage(code,
                                         args,
                                         code,
                                         resolveLocale());
    }

    private <T extends Throwable> T getCauseException(Throwable exception, Class<T> targetClass) {
        if (targetClass.isAssignableFrom(exception.getClass())) {
            return (T) exception;
        }
        else if (exception.getCause() == null) {
            return null;
        }
        else {
            return getCauseException(exception.getCause(), targetClass);
        }
    }


    private Locale resolveLocale() {
        return LocaleContextHolder.getLocale();
    }
}
