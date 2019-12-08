package com.qdo.votingapi.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.qdo.votingapi.responses.CustomApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomApiExceptionHandler {
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class,
            ApiNotFoundException.class,
            ApiTransactionException.class,
            ApiConflictException.class,
            InvalidFormatException.class,
    })
    public ResponseEntity<CustomApiErrorResponse> customHandleException(Exception ex, WebRequest request) throws Exception {
        if (ex instanceof ApiNotFoundException) {
            return customHandleExceptionInternal(ex, HttpStatus.NOT_FOUND);
        } else if (ex instanceof ApiTransactionException) {
            return customHandleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (ex instanceof ApiConflictException) {
            return customHandleExceptionInternal(ex, HttpStatus.CONFLICT);
        } else if (ex instanceof IllegalArgumentException) {
            return customHandleExceptionInternal(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof InvalidFormatException) {
            return customHandleExceptionInternal(ex, HttpStatus.BAD_REQUEST);
        } else if (ex instanceof MethodArgumentNotValidException) {
            return customHandleMethodArgumentNotValid((MethodArgumentNotValidException) ex, HttpStatus.BAD_REQUEST);
        }
        else {
            return customHandleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    protected ResponseEntity<CustomApiErrorResponse> customHandleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpStatus status) {
        List<String> errors = new ArrayList<String>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage() + ".");
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage() + ".");
        }

        CustomApiErrorResponse response = new CustomApiErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(errors);
        response.setStatus(status.value());

        return new ResponseEntity<>(response, status);
    }

    protected ResponseEntity<CustomApiErrorResponse> customHandleExceptionInternal(Exception ex, HttpStatus status) {
        CustomApiErrorResponse errors = new CustomApiErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage()); // + " " + ex.getClass().toString()
        errors.setStatus(status.value());

        return new ResponseEntity<>(errors, status);
    }
}
