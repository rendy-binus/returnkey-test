package com.example.returnkeytest.exception;

public class ServiceEncounteredErrorException extends RuntimeException {
    private static final long serialVersionUID = -6692151539805798816L;

    private ErrorCode errorCode;
    private String debugMessage;

    public ServiceEncounteredErrorException() {
    }

    public ServiceEncounteredErrorException(String message) {
        super(message);
    }

    public ServiceEncounteredErrorException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, String debugMessage) {
        super(message, cause);
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(Throwable cause, String debugMessage) {
        super(cause);
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String debugMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceEncounteredErrorException(Throwable cause) {
        super(cause);
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceEncounteredErrorException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceEncounteredErrorException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ServiceEncounteredErrorException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public ServiceEncounteredErrorException(ErrorCode errorCode, String debugMessage) {
        this.errorCode = errorCode;
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(String message, ErrorCode errorCode, String debugMessage) {
        super(message);
        this.errorCode = errorCode;
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, ErrorCode errorCode, String debugMessage) {
        super(message, cause);
        this.errorCode = errorCode;
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(Throwable cause, ErrorCode errorCode, String debugMessage) {
        super(cause);
        this.errorCode = errorCode;
        this.debugMessage = debugMessage;
    }

    public ServiceEncounteredErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode, String debugMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
        this.debugMessage = debugMessage;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
