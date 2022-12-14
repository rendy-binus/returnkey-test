package com.example.returnkeytest.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    RETURN_ERR_01("RETURN_ERR_01", "Token is not valid", HttpStatus.FORBIDDEN),
    RETURN_ERR_02("RETURN_ERR_02", "Token is expired", HttpStatus.FORBIDDEN),
    RETURN_ERR_03("RETURN_ERR_03", "Order Return not found", HttpStatus.NOT_FOUND),
    RETURN_ERR_04("RETURN_ERR_04", "Refund Item not found", HttpStatus.NOT_FOUND),
    RETURN_ERR_05("RETURN_ERR_05", "Refund Item Status can't be changed", HttpStatus.UNPROCESSABLE_ENTITY);

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
