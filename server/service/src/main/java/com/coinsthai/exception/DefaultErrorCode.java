package com.coinsthai.exception;

import org.springframework.http.HttpStatus;

public enum DefaultErrorCode implements ErrorCode {

    /* Default exception */
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST);

    DefaultErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
