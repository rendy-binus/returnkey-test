package com.example.returnkeytest.exception;

import com.example.returnkeytest.exception.model.ErrorResponse;
import com.example.returnkeytest.exception.model.SubError;
import com.example.returnkeytest.exception.model.ValidationError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<SubError> errorList = new ArrayList<>(ex.getBindingResult().getFieldErrorCount());
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            ValidationError error = ValidationError.builder()
                    .message(e.getDefaultMessage())
                    .object(e.getObjectName())
                    .field(e.getField())
                    .rejectedValue(e.getRejectedValue())
                    .build();
            errorList.add(error);
        });

        ErrorResponse error = ErrorResponse.builder()
                .message("Validation failed")
                .debugMessage(ex.getMessage())
                .subErrors(errorList)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        List<SubError> errorList = new ArrayList<>(ex.getConstraintViolations().size());
        ex.getConstraintViolations().forEach(e -> {
            ValidationError error = ValidationError.builder()
                    .message(e.getMessage())
                    .object(e.getRootBeanClass().getSimpleName())
                    .field(e.getPropertyPath().toString())
                    .rejectedValue(e.getInvalidValue())
                    .build();
            errorList.add(error);
        });

        ErrorResponse error = ErrorResponse.builder()
                .message("Violation on constraints")
                .debugMessage(ex.getMessage())
                .subErrors(errorList)
                .status(HttpStatus.BAD_REQUEST)
                .build();

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message("Could not execute request")
                .debugMessage(ex.getMostSpecificCause().getMessage())
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .build();

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message("Error parsing, cannot deserialize value")
                .status(HttpStatus.BAD_REQUEST)
                .debugMessage(ex.getMostSpecificCause().getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ServiceEncounteredErrorException.class)
    public ResponseEntity<ErrorResponse> handleServiceEncounteredError(ServiceEncounteredErrorException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .message(ex.getMessage())
                .debugMessage(ex.getDebugMessage())
                .status(ex.getErrorCode().getCode(), ex.getErrorCode().getDescription(), ex.getErrorCode().getHttpStatus())
                .build();

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(error);
    }
}
