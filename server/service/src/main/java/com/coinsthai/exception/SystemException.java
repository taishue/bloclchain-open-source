package com.coinsthai.exception;

import org.springframework.http.HttpStatus;

public class SystemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public static enum TYPE implements ErrorCode {

        // 禁止请求当前资源
        REQUEST_FORBIDDEN_ERROR(HttpStatus.FORBIDDEN),

        //
        EMAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),

        //
        DATABASE(HttpStatus.INTERNAL_SERVER_ERROR),

        //
        MQ(HttpStatus.INTERNAL_SERVER_ERROR),

        //
        SEARCH(HttpStatus.INTERNAL_SERVER_ERROR),

        // 未知错误
        UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR);

        private HttpStatus httpStatus;

        TYPE(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
        }

        @Override
        public HttpStatus getHttpStatus() {
            return httpStatus;
        }
    }

    private TYPE type;

    public SystemException(TYPE type) {
        super();
        this.type = type;
    }

    public SystemException(TYPE type, Throwable throwable) {
        super(throwable);
        this.type = type;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }
}
