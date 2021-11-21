package com.sssukho.disruptorlog.constant;

import org.springframework.http.HttpStatus;

public enum ErrorType {
    /* 알 수 없는 에러 */
    INTERNAL_SERVER_ERROR,
    /*  */
    NOT_FOUND_HEADER(HttpStatus.BAD_REQUEST),
    NOT_FOUND_TYPE(HttpStatus.BAD_REQUEST),
    NOT_FOUND_VERSION(HttpStatus.BAD_REQUEST),
    NOT_FOUND_HEADER_TYPE(HttpStatus.BAD_REQUEST),

    NOT_FOUND_BODY(HttpStatus.BAD_REQUEST),
    NOT_FOUND_COLUMN(HttpStatus.BAD_REQUEST),
    NOT_FOUND_CONDITION(HttpStatus.BAD_REQUEST),

    HEADER_TYPE_INVALID_DATA_TYPE(HttpStatus.BAD_REQUEST),
    HEADER_VERSION_INVALID_DATA_TYPE(HttpStatus.BAD_REQUEST),
    BODY_COLUMN_INVALID_DATA_TYPE(HttpStatus.BAD_REQUEST),
    BODY_CONDITION_INVALID_DATA_TYPE(HttpStatus.BAD_REQUEST),

    NOT_FOUND_META_INFO(HttpStatus.INTERNAL_SERVER_ERROR);

    private HttpStatus httpStatus;

    ErrorType() {
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    ErrorType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
