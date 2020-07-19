package com.coinsthai.common.exception;

import com.coinsthai.exception.DefaultErrorCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class AbstractResponseEntityExceptionHandler {
    protected final Log logger = LogFactory.getLog(getClass());

    /**
     * Log category to use when no mapped handler is found for a request.
     * @see #PAGE_NOT_FOUND_LOGGER
     */
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";

    /**
     * Additional logger to use when no mapped handler is found for a request.
     * @see #PAGE_NOT_FOUND_LOG_CATEGORY
     */
    protected static final Log PAGE_NOT_FOUND_LOGGER = LogFactory.getLog(PAGE_NOT_FOUND_LOG_CATEGORY);


    /**
     * Provides handling for standard Spring MVC exceptions.
     * @param exception the target exception
     * @param request the current request
     */
    @ExceptionHandler(value={
            NoSuchRequestHandlingMethodException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingServletRequestParameterException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestPartException.class,
            BindException.class,
            NoHandlerFoundException.class
    })

    public final ResponseEntity handleException(Exception exception, HttpServletRequest request) {

        HttpHeaders headers = new HttpHeaders();

        if (exception instanceof NoSuchRequestHandlingMethodException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleNoSuchRequestHandlingMethod((NoSuchRequestHandlingMethodException) exception, headers, status, request);
        }
        else if (exception instanceof HttpRequestMethodNotSupportedException) {
            HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
            return handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) exception, headers, status, request);
        }
        else if (exception instanceof HttpMediaTypeNotSupportedException) {
            HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            return handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) exception, headers, status, request);
        }
        else if (exception instanceof HttpMediaTypeNotAcceptableException) {
            HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
            return handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) exception, headers, status, request);
        }
        else if (exception instanceof MissingServletRequestParameterException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMissingServletRequestParameter((MissingServletRequestParameterException) exception, headers, status, request);
        }
        else if (exception instanceof ServletRequestBindingException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleServletRequestBindingException((ServletRequestBindingException) exception, headers, status, request);
        }
        else if (exception instanceof ConversionNotSupportedException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleConversionNotSupported((ConversionNotSupportedException) exception, headers, status, request);
        }
        else if (exception instanceof TypeMismatchException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleTypeMismatch((TypeMismatchException) exception, headers, status, request);
        }
        else if (exception instanceof HttpMessageNotReadableException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleHttpMessageNotReadable((HttpMessageNotReadableException) exception, headers, status, request);
        }
        else if (exception instanceof HttpMessageNotWritableException) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleHttpMessageNotWritable((HttpMessageNotWritableException) exception, headers, status, request);
        }
        else if (exception instanceof MethodArgumentNotValidException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMethodArgumentNotValid((MethodArgumentNotValidException) exception, headers, status, request);
        }
        else if (exception instanceof MissingServletRequestPartException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleMissingServletRequestPart((MissingServletRequestPartException) exception, headers, status, request);
        }
        else if (exception instanceof BindException) {
            HttpStatus status = HttpStatus.BAD_REQUEST;
            return handleBindException((BindException) exception, headers, status, request);
        }
        else if (exception instanceof NoHandlerFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            return handleNoHandlerFoundException((NoHandlerFoundException) exception, headers, status, request);
        }
        else {
            logger.warn("Unknown exception type: " + exception.getClass().getName());
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            return handleExceptionInternal(exception, headers, status, request);
        }
    }

    /**
     * A single place to customize the response body of all Exception types.
     * This method returns {@code null} by default.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     */
    protected ResponseEntity handleExceptionInternal(Exception exception,
                                                     HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", exception);
        }

        final ErrorContainer error = new ErrorContainer();
        error.setErrorCode(DefaultErrorCode.UNEXPECTED_ERROR);
        error.setErrorMessage(exception.getMessage());

        if (exception.getStackTrace() != null) {
            error.setDeveloperMessage(Arrays.toString(exception.getStackTrace()));
        }
        return new ResponseEntity(error, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Customize the response for NoSuchRequestHandlingMethodException.
     * This method logs a warning and delegates to
     * {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException exception,
                                                               HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        PAGE_NOT_FOUND_LOGGER.warn(exception.getMessage());

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for HttpRequestMethodNotSupportedException.
     * This method logs a warning, sets the "Allow" header, and delegates to
     * {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception,
                                                                 HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        PAGE_NOT_FOUND_LOGGER.warn(exception.getMessage());

        Set<HttpMethod> supportedMethods = exception.getSupportedHttpMethods();
        if (!supportedMethods.isEmpty()) {
            headers.setAllow(supportedMethods);
        }

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for HttpMediaTypeNotSupportedException.
     * This method sets the "Accept" header and delegates to
     * {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
                                                             HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        List<MediaType> mediaTypes = exception.getSupportedMediaTypes();
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            headers.setAccept(mediaTypes);
        }

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for HttpMediaTypeNotAcceptableException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception,
                                                              HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for MissingServletRequestParameterException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException exception,
                                                                  HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for ServletRequestBindingException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleServletRequestBindingException(ServletRequestBindingException exception,
                                                                  HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for ConversionNotSupportedException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleConversionNotSupported(ConversionNotSupportedException exception,
                                                          HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for TypeMismatchException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleTypeMismatch(TypeMismatchException exception, HttpHeaders headers,
                                                HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for HttpMessageNotReadableException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
                                                          HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for HttpMessageNotWritableException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleHttpMessageNotWritable(HttpMessageNotWritableException exception,
                                                          HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for MethodArgumentNotValidException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                          HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for MissingServletRequestPartException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleMissingServletRequestPart(MissingServletRequestPartException exception,
                                                             HttpHeaders headers, HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for BindException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    protected ResponseEntity handleBindException(BindException exception, HttpHeaders headers,
                                                 HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

    /**
     * Customize the response for NoHandlerFoundException.
     * This method delegates to {@link #handleExceptionInternal(Exception, HttpHeaders, HttpStatus, HttpServletRequest)}.
     * @param exception the exception
     * @param headers the headers to be written to the response
     * @param status the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     * @since 4.0
     */
    protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
                                                           HttpStatus status, HttpServletRequest request) {

        return handleExceptionInternal(exception, headers, status, request);
    }

}
