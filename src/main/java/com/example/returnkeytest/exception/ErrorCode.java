package com.example.returnkeytest.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RETURN_ERR_01("RETURN_ERR_01", "Token is not valid", HttpStatus.FORBIDDEN),
    RETURN_ERR_02("RETURN_ERR_02", "Token is expired", HttpStatus.FORBIDDEN);

    private final String code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ServiceEncounteredErrorException exception() {
        return new ServiceEncounteredErrorException(this.description, this);
    }

    public ServiceEncounteredErrorException exception(String debugMessage) {
        return new ServiceEncounteredErrorException(this.description, this, debugMessage);
    }
}
