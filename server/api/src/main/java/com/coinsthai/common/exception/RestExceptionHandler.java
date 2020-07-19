package com.coinsthai.common.exception;

import com.coinsthai.exception.DefaultErrorCode;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice("com.coinsthai.controller")
public class RestExceptionHandler extends AbstractExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractExceptionHandler.class);

    /**
     * This method handles business level exceptions.
     *
     * @param exception business exception being caught
     * @param request   http request
     */
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<ErrorContainer> exceptionHandler(DataAccessException exception, HttpServletRequest request) {
        LOGGER.error("Database level error detected in service layer.", exception);
        String dbErrorMsg = "";
        if (exception.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ce = (ConstraintViolationException) exception.getCause();
            dbErrorMsg = "a unique contraint violation is detected for " + ce.getConstraintName();
        }
        else {
            dbErrorMsg = exception.getMessage();
        }

        return getResponseEntity(exception,
                                 request,
                                 DefaultErrorCode.DATABASE_ERROR,
                                 new Object[]{dbErrorMsg},
                                 HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
