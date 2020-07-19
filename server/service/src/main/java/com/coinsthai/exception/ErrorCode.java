package com.coinsthai.exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.http.HttpStatus;

@JsonTypeInfo(use= JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface ErrorCode {
    /**
     * Resolve the http error code to use with this error.
     *
     * @return http status code
     */
    HttpStatus getHttpStatus();
}
